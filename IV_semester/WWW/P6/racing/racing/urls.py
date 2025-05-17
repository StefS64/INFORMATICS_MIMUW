"""
URL configuration for racing project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include
from django.contrib.auth import views as auth_views
from rest_framework.authtoken.views import obtain_auth_token
from drf_yasg.views import get_schema_view
from drf_yasg import openapi
from rest_framework import permissions
from mapeditor import views

schema_view = get_schema_view(
   openapi.Info(
      title="Racing API",
      default_version='v1',
   ),
   public=True,
   permission_classes=[permissions.AllowAny],  # <-- to pozwala na dostęp bez logowania
)

urlpatterns = [
    path('admin/', admin.site.urls),
    path('accounts/login/', auth_views.LoginView.as_view(), name='login'),
    path('accounts/logout/', auth_views.LogoutView.as_view(next_page='/'), name='logout'),
    path('', include('mapeditor.urls')),
    path('api/', include('mapeditor.api_urls')),
    path('api/auth/', include('rest_framework.urls')),  # browsable API login
    path('api/token/', obtain_auth_token),
    path('swagger/', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),
    path('board/<int:board_id>/edit/', views.edit_board, name='edit_board'),
    path('board/save/', views.save_board, name='save_board'),
]

