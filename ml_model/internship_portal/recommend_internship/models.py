from django.db import models

class Application(models.Model):
    max_exp_gap = models.FloatField(null=True, blank=True)
    min_exp_gap = models.FloatField(null=True, blank=True)
    is_exp_in_range = models.IntegerField(null = True, blank = True)
    user_experience = models.FloatField(null=True, blank=True)
    user_skills = models.TextField(null=True, blank=True)
    internship_field = models.CharField(max_length=50, null=True, blank=True)
    internship_sector = models.CharField(max_length=50, null=True, blank=True)
    internship_required_skills = models.TextField(null=True, blank=True)
    selection_status = models.CharField(max_length=30, null=True, blank=True)
    csv_ref_id = models.IntegerField(null=True, blank=True)
