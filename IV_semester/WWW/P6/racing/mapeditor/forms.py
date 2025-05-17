from django import forms
from .models import Route, Point

class RouteForm(forms.ModelForm):
    class Meta:
        model = Route
        fields = ['name', 'background']

class PointForm(forms.ModelForm):
    order = forms.IntegerField(required=False, label="Kolejność (opcjonalnie)")

    class Meta:
        model = Point
        fields = ['x', 'y', 'order']