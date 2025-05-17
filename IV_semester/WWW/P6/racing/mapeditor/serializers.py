from rest_framework import serializers
from .models import Route, Point

class PointSerializer(serializers.ModelSerializer):
    class Meta:
        model = Point
        fields = ['id', 'x', 'y', 'order']

class RouteSerializer(serializers.ModelSerializer):
    points = PointSerializer(many=True, read_only=True)
    background_name = serializers.CharField(source='background.name', read_only=True)
    background_image = serializers.ImageField(source='background.image', read_only=True)

    class Meta:
        model = Route
        fields = ['id', 'name', 'background', 'background_name', 'background_image', 'points']