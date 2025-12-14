from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json
from recommend_internship.ml.predictor import predict_user_for_internships


@csrf_exempt
def batch_predict_view(request):
    if request.method == "POST":
        # Decode request body
        body_bytes = request.body
        if not body_bytes:
            return JsonResponse({"error": "Empty body"}, status=400)
        try:
            body = json.loads(body_bytes.decode("utf-8"))
        except json.JSONDecodeError:
            return JsonResponse({"error": "Invalid JSON"}, status=400)
        # Extract data
        user_info = body.get("user_info")
        internships = body.get("internships")
        if not user_info or not internships:
            return JsonResponse({"error": "Missing user_info or internships"}, status=400)
        # Call your ML function
        results = predict_user_for_internships(user_info, internships)
        return JsonResponse(results, safe = False)

    return JsonResponse({"error": "Only POST allowed"}, status=400)