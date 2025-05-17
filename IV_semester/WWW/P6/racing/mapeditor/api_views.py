from rest_framework import viewsets, permissions, status
from rest_framework.response import Response
from rest_framework.decorators import action
from .models import Route, Point
from .serializers import RouteSerializer, PointSerializer
from django.shortcuts import get_object_or_404

class RouteViewSet(viewsets.ModelViewSet):
    serializer_class = RouteSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        # Swagger sets this attribute during schema generation
        if getattr(self, 'swagger_fake_view', False):
            return Route.objects.none()
        user = self.request.user
        if not user.is_authenticated:
            return Route.objects.none()
        return Route.objects.filter(user=user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    @action(detail=True, methods=['get', 'post'])
    def points(self, request, pk=None):
        route = self.get_object()
        if request.method == 'GET':
            points = route.points.all()
            return Response(PointSerializer(points, many=True).data)
        elif request.method == 'POST':
            serializer = PointSerializer(data=request.data)
            if serializer.is_valid():
                serializer.save(route=route)
                return Response(serializer.data, status=status.HTTP_201_CREATED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class PointViewSet(viewsets.ModelViewSet):
    serializer_class = PointSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        route_id = self.kwargs['route_pk']
        return Point.objects.filter(route__id=route_id, route__user=self.request.user)

    def perform_create(self, serializer):
        route = get_object_or_404(Route, id=self.kwargs['route_pk'], user=self.request.user)
        serializer.save(route=route)