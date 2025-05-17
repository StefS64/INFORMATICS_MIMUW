from django.urls import path
from . import views
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path('', views.home, name='home'),
    path('register/', views.register, name='register'),
    path('route/create/', views.create_route, name='create_route'),
    path('route/<int:route_id>/', views.route_detail, name='route_detail'),
    path('point/<int:point_id>/delete/', views.delete_point, name='delete_point'),
    path('board/create/', views.create_board, name='create_board'),
    path('board/<int:board_id>/edit/', views.edit_board, name='edit_board'),
    path('board/save/', views.save_board, name='save_board'),
    path('board/<int:board_id>/delete/', views.delete_board, name='delete_board'),
]

if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)