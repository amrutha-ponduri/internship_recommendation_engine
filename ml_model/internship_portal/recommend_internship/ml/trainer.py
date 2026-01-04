import os
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
import joblib
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error
from lightgbm import LGBMClassifier, LGBMRegressor


from .custom_encoders.MultiLabelBinarierWrapper import MultiLabelBinarizerWrapper


from .custom_encoders.KFoldTargetEncoder import KFoldTargetEncoder
from .custom_encoders.MultiValueKFoldEncoder import MultiValueKFoldEncoder
from recommend_internship.models import Application
from sklearn.model_selection import cross_val_score


BASE_DIR = os.path.dirname(__file__)  # points to recommend_internship/ml
MODEL_PATH = os.path.join(BASE_DIR, "model.pkl")
MLB_SKILLS_PATH = os.path.join(BASE_DIR, "mlb_skills.pkl")
MLB_REQ_PATH = os.path.join(BASE_DIR, "mlb_req.pkl")
FEATURES_PATH = os.path.join(BASE_DIR, "feature_cols.pkl")



def train_model():
    qs = Application.objects.all().values()
    data = pd.DataFrame(qs)

    # normalize empty strings
    data = data.applymap(
        lambda x: pd.NA if isinstance(x, str) and x.strip() == "" else x
    )

    # skill cols
    skills_cols = ['internship_required_skills', 'user_skills']

    all_skills = {
        skill.strip().lower()
        for col in skills_cols
        for skill_list in data[col]
        for skill in skill_list.split(',')
        if skill.strip()
    }

    data['user_skills_set'] = data['user_skills'].fillna('').apply(
        lambda x: set(s.strip().lower() for s in x.split(',') if s.strip())
    )

    data['req_skills_set'] = data['internship_required_skills'].fillna('').apply(
        lambda x: set(s.strip().lower() for s in x.split(',') if s.strip())
    )

    for skill in all_skills:
        data[f'match_skill_{skill}'] = data.apply(
            lambda r: int(skill in r['user_skills_set'] and skill in r['req_skills_set']),
            axis=1
        )

        data[f'missing_skill_{skill}'] = data.apply(
            lambda r: int(skill not in r['user_skills_set'] and skill in r['req_skills_set']),
            axis=1
        )


    # categorical handling
    user_cols = ['user_stream', 'user_specialization']
    internship_company_cols = ['internship_field', 'internship_sector']

    data[user_cols] = data[user_cols].fillna('Unknown')
    data[internship_company_cols] = data[internship_company_cols].fillna('Not Specified')

    # floats
    float_cols = [
        'user_experience',
        'internship_min_experience', 'internship_max_experience'
    ]

    for col in float_cols:
        data[col] = pd.to_numeric(data[col], errors='coerce')
        data[col] = data[col].fillna(data[col].median())

    data['exp_gap_min'] = data['user_experience'] - data['internship_min_experience']
    data['exp_gap_max'] = data['internship_max_experience'] - data['user_experience']
    data['exp_in_range'] = ((data['internship_min_experience'] - 1 <= data['user_experience']) & (data['user_experience'] <= data['internship_max_experience'] + 1)).astype(int)

    # drop rows with missing target
    data = data.dropna(subset=['selection_status'])


    data = data.drop(columns = ['id', 'date_of_reply', 'user_experience', 
                         'internship_max_experience', 'internship_min_experience',
                        'user_skills', 'internship_required_skills', 
                        'company', 'user_age', 'user_gender',
                        'user_qualification', 'user_state',
                        'user_district', 'internship_benefits',
                        'internship_max_stipend', 'internship_min_stipend',
                        'total_count', 'internship_min_qualification', 
                        'internship_required_age', 'required_gender'], axis = 1)

    # map target
    # target_scores = {
    #     'Application Rejected': 0,
    #     'Application Shortlisted': 1,
    #     'Rejected in interview': 3,
    #     'Selected': 6
    # }
    # data['selection_status'] = data['selection_status'].map(target_scores)

    cat_cols = [
        'user_stream',
        'user_specialization',
        'internship_field',
        'internship_sector'
    ]

    for col in cat_cols:
        data[col] = data[col].astype('category')

    preprocessor = ColumnTransformer(
        [
            ('cat_cols', KFoldTargetEncoder(cols = cat_cols), cat_cols)
        ]
    )

    X = data.drop(columns=['selection_status'])
    y = data['selection_status']

    pipeline = Pipeline([
        ('preprocessor', preprocessor),
        ('model', LGBMClassifier())
    ])

    scores = cross_val_score(pipeline, X, y, cv=5, scoring='accuracy')

    pipeline.fit(X, y)

    joblib.dump(pipeline, MODEL_PATH)

    print("CV MAE:", scores.mean())
    return scores.mean()
