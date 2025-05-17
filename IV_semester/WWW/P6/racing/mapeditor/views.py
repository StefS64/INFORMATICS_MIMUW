from django.contrib.auth.decorators import login_required
from django.contrib.auth.forms import UserCreationForm
from django.shortcuts import render, redirect
from django.contrib.auth import login
from .models import Route, BackgroundImage, Point
from .forms import RouteForm, PointForm
from django.shortcuts import get_object_or_404
from django.db.models import Max
from django.http import JsonResponse
import json
from .models import GameBoard, Dot
from django.views.decorators.csrf import csrf_exempt


def register(request):
    if request.method == "POST":
        form = UserCreationForm(request.POST)
        if form.is_valid():
            user = form.save()
            user.save()
            login(request, user)
            return redirect('home')
    else:
        form = UserCreationForm()
    return render(request, 'registration/register.html', {'form': form})

@login_required
def home(request):
    routes = Route.objects.filter(user=request.user)
    boards = GameBoard.objects.filter(user=request.user)
    return render(request, 'mapeditor/home.html', {'routes': routes, 'boards': boards})

@login_required
def create_route(request):
    if request.method == "POST":
        form = RouteForm(request.POST)
        if form.is_valid():
            route = form.save(commit=False)
            route.user = request.user
            route.save()
            return redirect('route_detail', route_id=route.id)
    else:
        form = RouteForm()
    backgrounds = BackgroundImage.objects.all()
    return render(request, 'mapeditor/create_route.html', {'form': form, 'backgrounds': backgrounds})

@login_required
def create_board(request):
    # Na razie tylko renderuje szablon, logika zapisu przez JS/AJAX
    return render(request, 'mapeditor/create_board.html')

@login_required
def route_detail(request, route_id):
    route = get_object_or_404(Route, id=route_id, user=request.user)
    points = route.points.all()

    if request.method == "POST":
        form = PointForm(request.POST)
        if form.is_valid():
            point = form.save(commit=False)
            point.route = route
            # If order is not specified, set to max+1
            if point.order in [None, '']:
                max_order = route.points.aggregate(Max('order'))['order__max'] or 0
                point.order = max_order + 1
            point.save()
            return redirect('route_detail', route_id=route.id)
    else:
        form = PointForm()

    return render(request, 'mapeditor/route_detail.html', {'route': route, 'points': points, 'form': form})

@login_required
def delete_point(request, point_id):
    point = get_object_or_404(Point, id=point_id, route__user=request.user)
    route_id = point.route.id
    if request.method == "POST":
        point.delete()
    return redirect('route_detail', route_id=route_id)

@csrf_exempt  # jeśli korzystasz z fetch bez CSRF, w produkcji lepiej obsłużyć CSRF!
@login_required
def save_board(request):
    if request.method == "POST":
        data = json.loads(request.body)
        board_id = data.get("id")
        name = data.get("name")
        rows = data.get("rows")
        cols = data.get("cols")
        dots = data.get("dots", [])
        if not (name and rows and cols):
            return JsonResponse({"error": "Brak wymaganych danych"}, status=400)
        if board_id:
            # Aktualizuj istniejącą planszę
            board = get_object_or_404(GameBoard, id=board_id, user=request.user)
            board.name = name
            board.rows = rows
            board.cols = cols
            board.save()
            board.dots.all().delete()
        else:
            # Utwórz nową planszę
            board = GameBoard.objects.create(user=request.user, name=name, rows=rows, cols=cols)
        for dot in dots:
            Dot.objects.create(
                board=board,
                row=dot["row"],
                col=dot["col"],
                color=dot["color"]
            )
        return JsonResponse({"success": True, "board_id": board.id})
    return JsonResponse({"error": "Tylko POST"}, status=405)

@login_required
def edit_board(request, board_id):
    board = get_object_or_404(GameBoard, id=board_id, user=request.user)
    dots = list(board.dots.values('row', 'col', 'color'))
    return render(request, 'mapeditor/create_board.html', {
        'edit': True,
        'board': board,
        'dots_json': json.dumps(dots),
    })

@login_required
def delete_board(request, board_id):
    board = get_object_or_404(GameBoard, id=board_id, user=request.user)
    if request.method == "POST":
        board.delete()
        return redirect('home')
    return render(request, 'mapeditor/confirm_board_delete.html', {'board': board})

