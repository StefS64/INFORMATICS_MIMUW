from django.test import TestCase, Client
from django.contrib.auth.models import User
from .models import BackgroundImage, Route, Point
from rest_framework.test import APIClient
from rest_framework.authtoken.models import Token

class ModelTests(TestCase):
    @classmethod
    def setUpTestData(cls):
        cls.user = User.objects.create_user(username='testuser', password='pass')
        cls.bg = BackgroundImage.objects.create(name='TestBG', image='test.jpg')
        cls.route = Route.objects.create(name='TestRoute', user=cls.user, background=cls.bg)
        cls.point = Point.objects.create(route=cls.route, x=10, y=20, order=1)

    def test_route_user_relation(self):
        self.assertEqual(self.route.user, self.user)

    def test_point_route_relation(self):
        self.assertEqual(self.point.route, self.route)

    def test_background_str(self):
        self.assertIn(self.bg.name, str(self.bg))

    def test_route_str(self):
        self.assertIn(self.route.name, str(self.route))

    def test_point_ordering(self):
        p2 = Point.objects.create(route=self.route, x=30, y=40, order=2)
        points = list(self.route.points.all())
        self.assertEqual(points[0], self.point)
        self.assertEqual(points[1], p2)

class AuthWebTests(TestCase):
    def setUp(self):
        self.client = Client()
        self.user = User.objects.create_user(username='testuser', password='pass')
        self.bg = BackgroundImage.objects.create(name='BG', image='bg.jpg')
        self.route = Route.objects.create(name='TestRoute', user=self.user, background=self.bg)

    def test_login_required_redirect(self):
        response = self.client.get(f'/route/{self.route.id}/')
        self.assertEqual(response.status_code, 302)  # Redirect to login

    def test_user_can_login(self):
        login = self.client.login(username='testuser', password='pass')
        self.assertTrue(login)

    def test_user_sees_own_routes(self):
        self.client.login(username='testuser', password='pass')
        response = self.client.get('/')
        self.assertContains(response, self.route.name)

    def test_create_route_view(self):
        self.client.login(username='testuser', password='pass')
        response = self.client.post('/route/create/', {'name': 'WebRoute', 'background': self.bg.id})
        self.assertEqual(response.status_code, 302)
        self.assertTrue(Route.objects.filter(name='WebRoute').exists())

    def test_add_and_delete_point(self):
        self.client.login(username='testuser', password='pass')
        # Add point
        response = self.client.post(f'/route/{self.route.id}/', {'x': 50, 'y': 60, 'order': 2})
        self.assertEqual(response.status_code, 302)
        self.assertTrue(Point.objects.filter(route=self.route, x=50, y=60).exists())
        # Delete point
        point = Point.objects.get(route=self.route, x=50, y=60)
        response = self.client.post(f'/point/{point.id}/delete/')
        self.assertEqual(response.status_code, 302)
        self.assertFalse(Point.objects.filter(id=point.id).exists())

    def test_cannot_access_others_route(self):
        other = User.objects.create_user(username='other', password='pass')
        other_route = Route.objects.create(name='OtherRoute', user=other, background=self.bg)
        self.client.login(username='testuser', password='pass')
        response = self.client.get(f'/route/{other_route.id}/')
        self.assertIn(response.status_code, [403, 404])

class APITests(TestCase):
    @classmethod
    def setUpTestData(cls):
        cls.user = User.objects.create_user(username='apitest', password='pass')
        cls.bg = BackgroundImage.objects.create(name='API BG', image='api.jpg')
        cls.route = Route.objects.create(name='API Route', user=cls.user, background=cls.bg)
        cls.token = Token.objects.create(user=cls.user)
        cls.api = APIClient()
        cls.api.credentials(HTTP_AUTHORIZATION='Token ' + cls.token.key)

    def test_api_auth_required(self):
        client = APIClient()
        response = client.get('/api/routes/')
        self.assertEqual(response.status_code, 401)

    def test_api_get_routes(self):
        response = self.api.get('/api/routes/')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.json()), 1)

    def test_api_create_route(self):
        data = {'name': 'NewRoute', 'background': self.bg.id}
        response = self.api.post('/api/routes/', data, format='json')
        self.assertEqual(response.status_code, 201)
        self.assertTrue(Route.objects.filter(name='NewRoute').exists())

    def test_api_get_route_detail(self):
        response = self.api.get(f'/api/routes/{self.route.id}/')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['id'], self.route.id)

    def test_api_delete_route(self):
        response = self.api.delete(f'/api/routes/{self.route.id}/')
        self.assertEqual(response.status_code, 204)
        self.assertFalse(Route.objects.filter(id=self.route.id).exists())

    def test_api_add_point(self):
        data = {'x': 1, 'y': 2, 'order': 1}
        response = self.api.post(f'/api/routes/{self.route.id}/points/', data, format='json')
        self.assertEqual(response.status_code, 201)
        self.assertTrue(Point.objects.filter(route=self.route, x=1, y=2).exists())

    def test_api_get_points(self):
        Point.objects.create(route=self.route, x=5, y=6, order=1)
        response = self.api.get(f'/api/routes/{self.route.id}/points/')
        self.assertEqual(response.status_code, 200)
        self.assertTrue(isinstance(response.json(), list))

    def test_api_wrong_user_access(self):
        other = User.objects.create_user(username='other', password='pass')
        other_route = Route.objects.create(name='OtherRoute', user=other, background=self.bg)
        response = self.api.get(f'/api/routes/{other_route.id}/')
        self.assertIn(response.status_code, [403, 404])

    def test_api_invalid_route_create(self):
        data = {'name': '', 'background': 9999}
        response = self.api.post('/api/routes/', data, format='json')
        self.assertEqual(response.status_code, 400)
        self.assertIn('background', response.json())
