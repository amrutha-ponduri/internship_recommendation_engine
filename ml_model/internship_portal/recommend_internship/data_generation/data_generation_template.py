import csv
from datetime import datetime, timedelta
from pathlib import Path
import random

import mysql.connector

from ..secretkeys import SecretKeys


class DataGenerationTemplate :

    def __init__(self, internships, additional_skills, companies, specializations, specialization_weights, stream, category = "") :
        
        self.internships = internships
        self.category = category
        self.additional_skills = additional_skills
        self.companies = companies
        self.specializations = specializations
        self.specialization_weights = specialization_weights
        self.districts_states = [
            {"district": "Thiruvananthapuram", "state": "Kerala"},
            {"district": "Kollam", "state": "Kerala"},
            {"district": "Pathanamthitta", "state": "Kerala"},
            {"district": "Alappuzha", "state": "Kerala"},
            {"district": "Kottayam", "state": "Kerala"},
            {"district": "Idukki", "state": "Kerala"},
            {"district": "Ernakulam", "state": "Kerala"},
            {"district": "Thrissur", "state": "Kerala"},
            {"district": "Palakkad", "state": "Kerala"},
            {"district": "Malappuram", "state": "Kerala"},
            {"district": "Kozhikode", "state": "Kerala"},
            {"district": "Wayanad", "state": "Kerala"},
            {"district": "Kannur", "state": "Kerala"},
            {"district": "Kasaragod", "state": "Kerala"},
            {"district": "Mumbai", "state": "Maharashtra"},
            {"district": "Pune", "state": "Maharashtra"},
            {"district": "Nagpur", "state": "Maharashtra"},
            {"district": "Thane", "state": "Maharashtra"},
            {"district": "Nashik", "state": "Maharashtra"},
            {"district": "Aurangabad", "state": "Maharashtra"},
            {"district": "Solapur", "state": "Maharashtra"},
            {"district": "Amravati", "state": "Maharashtra"},
            {"district": "Kolhapur", "state": "Maharashtra"},
            {"district": "Jalgaon", "state": "Maharashtra"},
            {"district": "Ahmednagar", "state": "Maharashtra"},
            {"district": "Raigad", "state": "Maharashtra"},
            {"district": "Bengaluru Urban", "state": "Karnataka"},
            {"district": "Bengaluru Rural", "state": "Karnataka"},
            {"district": "Mysuru", "state": "Karnataka"},
            {"district": "Mangaluru", "state": "Karnataka"},
            {"district": "Hubballi-Dharwad", "state": "Karnataka"},
            {"district": "Belagavi", "state": "Karnataka"},
            {"district": "Kalaburagi", "state": "Karnataka"},
            {"district": "Ballari", "state": "Karnataka"},
            {"district": "Shivamogga", "state": "Karnataka"},
            {"district": "Tumakuru", "state": "Karnataka"},
            {"district": "Chennai", "state": "Tamil Nadu"},
            {"district": "Coimbatore", "state": "Tamil Nadu"},
            {"district": "Madurai", "state": "Tamil Nadu"},
            {"district": "Tiruchirappalli", "state": "Tamil Nadu"},
            {"district": "Salem", "state": "Tamil Nadu"},
            {"district": "Erode", "state": "Tamil Nadu"},
            {"district": "Tirunelveli", "state": "Tamil Nadu"},
            {"district": "Vellore", "state": "Tamil Nadu"},
            {"district": "Thanjavur", "state": "Tamil Nadu"},
            {"district": "Kanchipuram", "state": "Tamil Nadu"},
            {"district": "Agra", "state": "Uttar Pradesh"},
            {"district": "Lucknow", "state": "Uttar Pradesh"},
            {"district": "Kanpur", "state": "Uttar Pradesh"},
            {"district": "Varanasi", "state": "Uttar Pradesh"},
            {"district": "Meerut", "state": "Uttar Pradesh"},
            {"district": "Ghaziabad", "state": "Uttar Pradesh"},
            {"district": "Noida", "state": "Uttar Pradesh"},
            {"district": "Aligarh", "state": "Uttar Pradesh"},
            {"district": "Mathura", "state": "Uttar Pradesh"},
            {"district": "Bareilly", "state": "Uttar Pradesh"},
            {"district": "Jaipur", "state": "Rajasthan"},
            {"district": "Jodhpur", "state": "Rajasthan"},
            {"district": "Udaipur", "state": "Rajasthan"},
            {"district": "Kota", "state": "Rajasthan"},
            {"district": "Bikaner", "state": "Rajasthan"},
            {"district": "Ajmer", "state": "Rajasthan"},
            {"district": "Alwar", "state": "Rajasthan"},
            {"district": "Sikar", "state": "Rajasthan"},
            {"district": "Bhilwara", "state": "Rajasthan"},
            {"district": "Jaipur Rural", "state": "Rajasthan"},
            {"district": "Kolkata", "state": "West Bengal"},
            {"district": "Darjeeling", "state": "West Bengal"},
            {"district": "Howrah", "state": "West Bengal"},
            {"district": "Hooghly", "state": "West Bengal"},
            {"district": "North 24 Parganas", "state": "West Bengal"},
            {"district": "South 24 Parganas", "state": "West Bengal"},
            {"district": "Siliguri", "state": "West Bengal"},
            {"district": "Bardhaman", "state": "West Bengal"},
            {"district": "Medinipur", "state": "West Bengal"},
            {"district": "Malda", "state": "West Bengal"},
            {"district": "Hyderabad", "state": "Telangana"},
            {"district": "Warangal", "state": "Telangana"},
            {"district": "Karimnagar", "state": "Telangana"},
            {"district": "Nizamabad", "state": "Telangana"},
            {"district": "Khammam", "state": "Telangana"},
            {"district": "Rangareddy", "state": "Telangana"},
            {"district": "Adilabad", "state": "Telangana"},
            {"district": "Nalgonda", "state": "Telangana"},
            {"district": "Mahbubnagar", "state": "Telangana"},
            {"district": "Suryapet", "state": "Telangana"},
            {"district": "Bhopal", "state": "Madhya Pradesh"},
            {"district": "Indore", "state": "Madhya Pradesh"},
            {"district": "Gwalior", "state": "Madhya Pradesh"},
            {"district": "Jabalpur", "state": "Madhya Pradesh"},
            {"district": "Ujjain", "state": "Madhya Pradesh"},
            {"district": "Dewas", "state": "Madhya Pradesh"},
            {"district": "Satna", "state": "Madhya Pradesh"},
            {"district": "Sagar", "state": "Madhya Pradesh"},
            {"district": "Rewa", "state": "Madhya Pradesh"},
            {"district": "Ratlam", "state": "Madhya Pradesh"}
        ]
        self.stream = stream
        path = f'D:/internship-recommendation-engine/ml_model/internship_portal/recommend_internship/synthetic_table_data/'
        base_path = Path(path)
        new_folder = base_path / self.category 
        new_folder.mkdir(parents = True, exist_ok = True)
        self.path = path + self.category + '/'

    def generate_user_records(self, start) :

        users_data = []
        user_skill_data = []
        users_file = self.path + f'user_{self.category}.csv'
        user_skill_file = self.path + f'user_skill_{self.category}.csv'
        self.fetch_skill_ids()

        user_genders = ['Male', 'Female', 'Not specified']
        user_names = [
            "Aarav", "Aditi", "Aishwarya", "Akash", "Amit",
            "Ananya", "Anil", "Anita", "Arjun", "Ashok",
            "Bhavya", "Chaitanya", "Deepak", "Divya", "Gaurav",
            "Ishaan", "Kavya", "Kiran", "Lakshmi", "Manish",
            "Meera", "Mohit", "Neha", "Nikhil", "Pooja",
            "Prakash", "Priya", "Rahul", "Rajesh", "Ramesh",
            "Ravi", "Riya", "Rohit", "Sakshi", "Sandeep",
            "Sanjay", "Shalini", "Sharma", "Shivam", "Shruti",
            "Sneha", "Sonal", "Sunil", "Suresh", "Swati",
            "Tanvi", "Varun", "Vikas", "Vivek", "Yash",
            "Abhishek", "Aditya", "Ankur", "Bharti", "Chetan",
            "Dinesh", "Ekta", "Harsh", "Hemant", "Jyoti",
            "Kunal", "Madhav", "Naveen", "Nisha", "Omkar",
            "Pankaj", "Ritu", "Saurabh", "Seema", "Shubham",
            "Smita", "Srinivas", "Tejas", "Usha", "Vandana",
            "Vinay", "Vijay", "Anup", "Ashwin", "Bhushan",
            "Ganesh", "Keerthi", "Lokesh", "Mounika", "Pranav",
            "Rohini", "Siddharth", "Uma", "Vaishnavi", "Zoya"
        ]

        j = start
        for internship in self.internships :
            for _ in range(4) :
                # user data
                user_record = {}
                user_skills_domain = internship['must_have'] + random.sample(self.additional_skills, k=random.randint(0, len(self.additional_skills)))
                user_skills = self.generate_user_skills(user_skills_domain)
                for skill in user_skills :
                    user_skill_record = {}
                    user_skill_record["user_id"] = j
                    user_skill_record["skill_id"] = self.skill_id[self.skill_id_map[skill]]
                    user_skill_data.append(user_skill_record)

                user_record['experience'] = random.randint(0, internship['max_experience'] + 5)
                user_record['name'] = random.choice(user_names)
                user_record['age'] = random.randint(18, 30)
                user_record['highest_qualfication_rank'] = random.randint(3, 6)
                location = random.choice(self.districts_states)
                user_record['district'] = location['district']
                user_record['state'] = location['state']
                user_record['stream'] = self.stream
                user_record['gender'] = random.choice(user_genders)
                user_record['external_id'] = j
                user_record['id'] = j
                user_record['specialization'] = random.choices(self.specializations, weights = self.specialization_weights, k = 1)[0]
                j += 1 
                users_data.append(user_record)   
        
        self.write_records(users_file, users_data)
        self.write_records(user_skill_file, user_skill_data)

    def generate_internship_records(self, titles, start) :

        benefits = [
            "Certificate", "Stipend", "PPE Provided","Travel allowance",
            "Exposure to Banking",
            "Mentorship",
            "Meals",
            "Employee discount",
            "Letter of Recommendation",
            "Hands-on training",
            "Exposure to R&D",
            "Equity Consideration",
            "Accommodation"]
    
        modes_list = ['Onsite', 'Hybrid', 'Remote']
        modes_weights = [3, 1, 4]

        internships_data = []
        internship_requirements_data = []
        internship_skill_data = []
        internships_file = self.path + f'internship_{self.category}.csv'
        internship_requirements_file = self.path + f'internship_requirements_{self.category}.csv'
        internship_skill_file = self.path + f'internship_skill_{self.category}.csv'

        self.fetch_skill_ids()

        company_id = self.fetch_company_ids()

        j = start
        for internship in self.internships :
            for _ in range(2) :
            
                internship_requirements_record = {}
                internship_record = {}
                internship_record["internship_description"] = f"This internship focuses on {', '.join(internship['required'][:3])} and related frontend development tasks."
                internship_requirements_record["requirements_responsibilities_description"] = (
                    f"Required skills: {', '.join(internship['required'])}. "
                    f"Must-have skills: {', '.join(internship['must_have'])}. "
                    f"Interns will be responsible for tasks involving {', '.join(internship['required'][:3])} "
                    f"and assisting in related projects to gain hands-on experience."
                )

                for skill in internship["required"] :
                    internship_skill = {}
                    internship_skill["internship_id"] = j
                    internship_skill["skill_id"] = self.skill_id[self.skill_id_map[skill]] 
                    internship_skill_data.append(internship_skill)

                total_count = random.randint(5, 31)
                applied_count = random.randint(0, total_count + 1)
                internship_record['applied_count'] = applied_count
                internship_record['total_count'] = total_count
                internship_record['benefits'] = random.sample(benefits, k = random.randint(1, 5))
                internship_record['title'] = random.choice(titles)
                internship_record['field'] = " ".join(self.category.split("_"))
                location = random.choice(self.districts_states)
                internship_record['district'] = location['district']
                internship_record['state'] = location['state']
                max_stipend = random.randint(0, 15001)
                internship_record['max_stipend'] = float(max_stipend)
                internship_record['min_stipend'] = float(random.randint(0, max_stipend // 2 + 1))
                internship_record['duration'] = str(random.randint(3, 9)) + "months"
                internship_record['sector'] = 'Technology'
                date1 = datetime(2024, 3, 1)
                date2 = datetime(2026, 1, 1)
                internship_record['posting_time'] = self.get_random_date(date1, date2)
                internship_record['company_id'] = company_id[random.choice(self.companies)['external_id']]
                internship_record['external_id'] = j
                internship_record['id'] = j

                max_experience = random.randint(1, 4)
                internship_requirements_record['max_experience'] = max_experience
                internship_requirements_record['min_experience'] = random.randint(0, max_experience + 1)
                internship_requirements_record['min_qualification_rank'] = random.randint(3, 6)
                internship_requirements_record['mode'] = random.choices(modes_list, weights=modes_weights, k = 1)[0]
                internship_requirements_record['stream'] = self.stream
                requirements_sepcializations = self.specializations + ['Any']
                requirements_sepcializations_weights = self.specialization_weights + [10]
                internship_requirements_record['specialization'] = random.choices(requirements_sepcializations, weights = requirements_sepcializations_weights, k = 1)[0]
                internship_requirements_record["external_id"] = j
                internship_requirements_record['internship_id'] = j
                internship_requirements_data.append(internship_requirements_record)
                internships_data.append(internship_record)
                j += 1

        self.write_records(internship_skill_file, internship_skill_data)
        self.write_records(internships_file, internships_data)
        self.write_records(internship_requirements_file, internship_requirements_data)
        
    def generate_skill_records(self) :
        skill_set = self.fetch_skill_ids()
        skill_records = self.fit_skill_set(skill_set)
        skills_file = self.path + f'skills_{self.category}.csv'

        self.write_records(skills_file, skill_records)
        
    def generate_selection_records(self, start) :

        selection_data = []
        training_path = 'D:/internship-recommendation-engine/ml_model/internship_portal/recommend_internship/training_data/' 
        selection_records_file = training_path + f'selection_{self.category}.csv'
        j = start
        for internship in self.internships:
            for _ in range(10):
                user_skills_list = self.generate_user_skills(internship['required'])
                
                selection_record = {
                    'user_skills': ",".join(user_skills_list),
                    'user_experience': random.randint(0, internship['max_experience'] + 1),
                    'internship_field': " ".join(self.category.split("_")),
                    'internship_sector': 'Technology',
                    'internship_required_skills': ",".join(internship['required']),
                    'is_selected': 0
                }

                # Experience gaps
                selection_record['max_exp_gap'] = internship['max_experience'] - selection_record['user_experience']
                selection_record['min_exp_gap'] = selection_record['user_experience'] - internship['min_experience']

                # Is experience in range?
                selection_record['is_exp_in_range'] = int(
                    internship['min_experience'] - 1 <= selection_record['user_experience'] <= internship['max_experience'] + 1
                )

                # Selection logic
                prob = 0.15
                if self.is_skill_match(set(user_skills_list), set(internship['must_have'])):
                    prob += 0.45
                if selection_record['is_exp_in_range']:
                    prob += 0.25

                if prob == 0.85 :
                    selection_record['is_selected'] = 1
                elif prob >= 0.60 :
                    selection_weights = [10, 2]
                    selection = [1, 0]
                    selection_record['is_selected'] = random.choices(selection, weights=selection_weights)[0]
                else :
                    selection_weights = [1, 6]
                    selection = [1, 0]
                    selection_record['is_selected'] = random.choices(selection, weights=selection_weights)[0]

                selection_record['csv_ref_id'] = j
                j += 1
                selection_data.append(selection_record)

        self.write_records(selection_records_file, selection_data)

    def write_records(self, file_path, data) :
        if (data) :
            with open(file_path, mode='w', newline = '', encoding='utf-8') as file :
                fieldnames = data[0].keys()
                writer = csv.DictWriter(file, fieldnames=fieldnames)
                writer.writeheader()
                writer.writerows(data)

    def fetch_company_ids(self) :

        company_id = {}

        conn = mysql.connector.connect(
            host = 'localhost',
            user = SecretKeys.mysql_username,
            password = SecretKeys.mysql_password,
            database = SecretKeys.mysql_database_name
        )
        cursor = conn.cursor()
        cursor.execute('SELECT id, csv_ref_id FROM company')
        rows = cursor.fetchall()
        for row in rows :
            company_id[row[1]] = row[0]
        cursor.close()
        conn.close()

        return company_id
    
    def fetch_skill_ids(self) :

        conn = mysql.connector.connect(
            host = 'localhost',
            user = SecretKeys.mysql_username,
            password = SecretKeys.mysql_password,
            database = SecretKeys.mysql_database_name
        )
        self.skill_id = {}
        self.skill_id_map = {}
        skill_set = set()

        cursor = conn.cursor()
        cursor.execute('SELECT id, csv_ref_id, skill_name FROM skills')
        rows = cursor.fetchall()
        for row in rows :
            self.skill_id[row[1]] = row[0]
            self.skill_id_map[row[2]] = row[1]
            skill_set.add(row[2])

        cursor.close()
        conn.close()
        return skill_set
    
    def fit_skill_set(self, skill_set) :
        i = len(skill_set) + 1
        skill_records = []

        for internship in self.internships :
            for skill in internship['required'] + self.additional_skills :
                if (skill not in skill_set) :
                    skill_record = {}
                    skill_set.add(skill)
                    skill_record['name'] = skill
                    skill_record['external_id'] = i
                    skill_records.append(skill_record)
                    i += 1

        return skill_records
    
    def generate_user_skills(self, required_skills, min_skills = 2) :
        min_skills = min(min_skills, len(required_skills))
        k = random.randint(min_skills, len(required_skills))
        return random.sample(required_skills, k)
    
    def is_skill_match(self, user_skills, required_skills) :
        return required_skills.issubset(user_skills)

    def get_random_date(self, date1, date2) :
        gap = (date2 - date1).days
        random_gap = random.randint(0, gap + 1)
        return date1 + timedelta(days=random_gap)