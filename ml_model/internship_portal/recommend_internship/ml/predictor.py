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

categorical_cols = ["gender", "user_location", "internship_location", "company", "sector", "role"]

def predict_user_for_internships(user_info: dict, internships: list):
    """
    user_info: dict with user data
    internships: list of dicts, each containing internship data including internship_id
    Returns: dict mapping internship_id -> predicted probability
    """
    results = []

    for internship in internships:
        # Combine user and internship info
        data = {**user_info, **internship}
        df = pd.DataFrame([data])

        prob = float(pipeline.predict_proba(df)[0][1])
        results.append({
            "internship_id": data["internship_id"],
            "score": prob
        })

    return results