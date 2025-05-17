from django.contrib import admin
from .models import BackgroundImage, Route, Point, GameBoard, Dot

admin.site.register(BackgroundImage)
admin.site.register(GameBoard)
admin.site.register(Dot)
admin.site.register(Route)
admin.site.register(Point)
