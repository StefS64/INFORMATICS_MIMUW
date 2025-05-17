from django.db import models
from django.contrib.auth.models import User

class BackgroundImage(models.Model):
    name = models.CharField(max_length=100)
    image = models.ImageField(upload_to='backgrounds/')

    def __str__(self):
        return f"{self.name} ({self.image.url if self.image else 'No Image'})"

class Route(models.Model):
    name = models.CharField(max_length=100)
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    background = models.ForeignKey(BackgroundImage, on_delete=models.CASCADE)

    def __str__(self):
        return f"{self.name} ({self.user.username})"

class Point(models.Model):
    route = models.ForeignKey(Route, related_name='points', on_delete=models.CASCADE)
    x = models.IntegerField()
    y = models.IntegerField()
    order = models.IntegerField()

    class Meta:
        ordering = ['order']

class GameBoard(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    name = models.CharField(max_length=100)
    rows = models.PositiveIntegerField()
    cols = models.PositiveIntegerField()

    def __str__(self):
        return f"{self.name} ({self.user.username})"

class Dot(models.Model):
    board = models.ForeignKey(GameBoard, related_name='dots', on_delete=models.CASCADE)
    row = models.PositiveIntegerField()
    col = models.PositiveIntegerField()
    color = models.CharField(max_length=7)  # np. "#FF0000"

    def __str__(self):
        return f"Dot ({self.row}, {self.col}) {self.color} on {self.board.name}"

