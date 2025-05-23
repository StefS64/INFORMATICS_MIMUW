from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .api_views import RouteViewSet

router = DefaultRouter()
router.register(r'routes', RouteViewSet, basename='route')

urlpatterns = [
    path('', include(router.urls)),
]