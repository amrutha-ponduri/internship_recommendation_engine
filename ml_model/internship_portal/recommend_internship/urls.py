from django.urls import path
from . import views

urlpatterns = [
    path("batch_predict/", views.batch_predict_view, name="batch_predict"),  # batch
]