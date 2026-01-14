import os
import pandas as pd
import joblib

BASE_DIR = os.path.dirname(__file__)
MODEL_PATH = os.path.join(BASE_DIR, "model.pkl")
pipeline = joblib.load(MODEL_PATH)

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
        user_experience = df['user_experience'][0]

        # Ensure multi-hot columns are lists
        for col in ['user_skills', 'internship_required_skills']:
            df[col] = df[col].apply(lambda x: x if isinstance(x, list) else [])

        df['min_exp_gap'] = abs(df['internship_min_experience'] - user_experience)
        df['max_exp_gap'] = abs(df['internship_max_experience'] - user_experience)
        df['is_exp_in_range'] = (
            (df['internship_min_experience'] <= user_experience) &
            (user_experience <= df['internship_max_experience'])
        ).astype(int)


        df = df.drop(columns=['internship_min_experience', 'internship_max_experience', 'user_experience'])

        proba = pipeline.predict_proba(df)[0][1]
        results.append({
            "internship_id": data["internship_id"],
            "score": proba
        })


    return results