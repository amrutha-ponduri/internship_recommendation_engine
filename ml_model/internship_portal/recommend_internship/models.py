from django.db import models

class SelectionRecords(models.Model):
    id = models.AutoField(primary_key=True)

    max_exp_gap = models.FloatField()
    min_exp_gap = models.FloatField()
    is_exp_in_range = models.IntegerField()

    user_experience = models.FloatField()

    user_skills = models.TextField()  # @Lob → TextField

    internship_field = models.CharField(max_length=255)
    internship_sector = models.CharField(max_length=255)

    internship_required_skills = models.TextField()  # @Lob → TextField

    selection_status = models.IntegerField()
    csv_ref_id = models.IntegerField()

    class Meta:
        managed = False            # Django will NOT manage schema
        db_table = "selection_records"
