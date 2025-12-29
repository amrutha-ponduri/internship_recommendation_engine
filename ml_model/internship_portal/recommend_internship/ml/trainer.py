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
from lightgbm import LGBMClassifier


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

    # categorical handling
    user_cols = ['user_gender', 'user_stream', 'user_specialization', 'user_state', 'user_district']
    internship_company_cols = ['internship_field', 'internship_sector', 'company', 'required_gender']

    data[user_cols] = data[user_cols].fillna('Unknown')
    data[internship_company_cols] = data[internship_company_cols].fillna('Not Specified')

    # continuous integers
    for col in ['user_age', 'internship_required_age']:
        data[col] = pd.to_numeric(data[col], errors='coerce')
        data[col] = data[col].fillna(data[col].median()).round().astype(int)

    # ordinal integers
    data[['user_qualification', 'internship_min_qualification']] = \
        data[['user_qualification', 'internship_min_qualification']].fillna(-1)

    # floats
    float_cols = [
        'user_experience',
        'internship_min_stipend', 'internship_max_stipend',
        'internship_min_experience', 'internship_max_experience'
    ]
    for col in float_cols:
        data[col] = pd.to_numeric(data[col], errors='coerce')
        data[col] = data[col].fillna(data[col].median())

    # drop rows with missing target
    data = data.dropna(subset=['selection_status'])

    # map target
    # target_scores = {
    #     'Application Rejected': 0,
    #     'Application Shortlisted': 1,
    #     'Rejected in interview': 3,
    #     'Selected': 6
    # }
    # data['selection_status'] = data['selection_status'].map(target_scores)

    # multi-hot columns
    multi_hot_cols = [
        "internship_benefits",
        "internship_required_skills",
        "user_skills"
    ]

    for col in multi_hot_cols:
        data[col] = data[col].fillna("").astype(str).apply(
            lambda x: [] if x.strip() == "" else
            [i.strip() for i in x.split(",") if i.strip()]
        )

    X = data.drop(columns=['id', 'selection_status', 'date_of_reply'])
    y = data['selection_status']

    categorical_cols = ["user_gender", "user_stream", "user_specialization", "user_district", "user_state", "internship_field", "internship_sector", "company", "required_gender"] 
    numerical_cols = ["user_age", "user_experience", "user_qualification", "internship_min_stipend", "internship_max_stipend", "total_count", "internship_min_experience", "internship_max_experience", "internship_min_qualification", "internship_required_age"] 
    multi_hot_cols = ["internship_benefits", "internship_required_skills", "user_skills"]

    preprocessor = ColumnTransformer(
        transformers=[
            # ('cat', KFoldTargetEncoder(cols=categorical_cols), categorical_cols),
            ('multi', MultiLabelBinarizerWrapper(cols=multi_hot_cols), multi_hot_cols),
            ('num', StandardScaler(), numerical_cols)
        ]
    )

    pipeline = Pipeline([
        ('preprocessor', preprocessor),
        ('model', LGBMClassifier())
    ])

    scores = cross_val_score(pipeline, X, y, cv=5, scoring='accuracy')
    pipeline.fit(X, y)

    joblib.dump(pipeline, MODEL_PATH)

    print("CV accuracy:", scores.mean())
    return scores.mean()
