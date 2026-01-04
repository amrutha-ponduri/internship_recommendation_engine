import os
import pandas as pd
import joblib

BASE_DIR = os.path.dirname(__file__)
MODEL_PATH = os.path.join(BASE_DIR, "model.pkl")
MLB_SKILLS_PATH = os.path.join(BASE_DIR, "mlb_skills.pkl")
MLB_REQ_PATH = os.path.join(BASE_DIR, "mlb_req.pkl")
FEATURES_PATH = os.path.join(BASE_DIR, "feature_cols.pkl")


pipeline = joblib.load(MODEL_PATH)
mlb = joblib.load(MLB_SKILLS_PATH)
mlb_req = joblib.load(MLB_REQ_PATH)
feature_cols = joblib.load(FEATURES_PATH)

def predict_user_for_internships(user_info: dict, internships: list):
    """
    user_info: dict with user data
    internships: list of dicts, each containing internship data including internship_id
    Returns: dict mapping internship_id -> predicted probability
    """
    target_scores = {'Application Rejected': 0, 'Application Shortlisted': 1, 'Rejected in interview': 3, 'Selected': 6}
    results = []

    for internship in internships:
        # Combine user and internship info
        data = {**user_info, **internship}
        df = pd.DataFrame([data])

        # Ensure multi-hot columns are lists
        for col in ['user_skills', 'internship_required_skills', 'internship_benefits']:
            df[col] = df[col].apply(lambda x: x if isinstance(x, list) else [])

        proba = pipeline.predict_proba(df)[0]
        classes = pipeline.classes_

        # Weighted score
        score = sum(p * target_scores[c] for p, c in zip(proba, classes))

        results.append({
            "internship_id": data["internship_id"],
            "score": score
        })


    return results