from .data_generation_template import DataGenerationTemplate

# Running this file : python -m internship_portal.recommend_internship.data_generation.data_generation

frontend_internships = [
    {
        "id": 1,
        "required": ["framer", "html", "css", "js", "java", "mysql"],
        "must_have": ["html", "css", "js", "framer"],
        "min_experience" : 1,
        "max_experience" : 3
    },
    {
        "id": 2,
        "required": ["html", "css", "js", "jquery", "photoshop", "java basics", "java advanced"],
        "must_have": ["html", "css", "js", "photoshop"],
        "min_experience" : 0,
        "max_experience" : 3

    },
    {
        "id": 3,
        "required": ["css", "backend", "frontend", "git", "github", "html", "js", "node"],
        "must_have": ["html", "css", "js"],
        "min_experience" : 0,
        "max_experience" : 1
    },
    {
        "id": 4,
        "required": ["html", "css", "js", "frontend", "mysql"],
        "must_have": ["html", "css", "js"],
        "min_experience" : 1,
        "max_experience" : 2
    },
    {
        "id": 5,
        "required": ["html", "css", "js", "react", "vue", "angular", "git", "github"],
        "must_have": ["html", "css", "js"],
        "min_experience" : 0,
        "max_experience" : 4
    },
    {
        "id": 6,
        "required": ["html", "css", "js", "figma", "ui/ux"],
        "must_have": ["html", "css", "js", "figma"],
        "min_experience" : 0,
        "max_experience" : 1
    },
    {
        "id": 7,
        "required": ["css", "typescript", "js", "html", "react"],
        "must_have": ["html", "css", "js", "react"],
        "min_experience" : 0,
        "max_experience" : 2
    },
    {
        "id": 8,
        "required": ["html", "css", "js", "jquery", "php", "react", "next"],
        "must_have": ["html", "css", "js", "react"],
        "min_experience" : 2,
        "max_experience" : 5
    },
    {
        "id": 9,
        "required": ["html", "css", "js", "react", "responsive web design", "angular"],
        "must_have": ["html", "css", "js", "responsive web design"],
        "min_experience" : 0,
        "max_experience" : 2
    },
    {
        "id": 10,
        "required": ["lovable", "html", "css", "js", "react"],
        "must_have": ["html", "css", "js", "lovable"],
        "min_experience" : 1,
        "max_experience" : 6
    }
]

cs_companies = [
    # ---------- Tier 1 : MAANG / Top Product ----------
    {
        'external_id': 1,
        "company_name": "Google India",
        "image_url": "https://logo.clearbit.com/google.com",
        "company_tier": 1
    },
    {
        'external_id': 2,
        "company_name": "Amazon India",
        "image_url": "https://logo.clearbit.com/amazon.in",
        "company_tier": 1
    },
    {
        'external_id': 3,
        "company_name": "Microsoft India",
        "image_url": "https://logo.clearbit.com/microsoft.com",
        "company_tier": 1
    },
    {
        'external_id': 4,
        "company_name": "Meta",
        "image_url": "https://logo.clearbit.com/meta.com",
        "company_tier": 1
    },
    {
        'external_id': 5,
        "company_name": "Apple India",
        "image_url": "https://logo.clearbit.com/apple.com",
        "company_tier": 1
    },

    # ---------- Tier 2 : Service-based / Strong Indian Companies ----------
    {
        'external_id': 6,
        "company_name": "Infosys",
        "image_url": "https://logo.clearbit.com/infosys.com",
        "company_tier": 2
    },
    {
        'external_id': 7,
        "company_name": "Tata Consultancy Services",
        "image_url": "https://logo.clearbit.com/tcs.com",
        "company_tier": 2
    },
    {
        'external_id': 8,
        "company_name": "Wipro",
        "image_url": "https://logo.clearbit.com/wipro.com",
        "company_tier": 2
    },
    {
        'external_id': 9,
        "company_name": "Accenture",
        "image_url": "https://logo.clearbit.com/accenture.com",
        "company_tier": 2
    },
    {
        'external_id': 10,
        "company_name": "Flipkart",
        "image_url": "https://logo.clearbit.com/flipkart.com",
        "company_tier": 2
    },
    {
        'external_id': 11,
        "company_name": "Cognizant",
        "image_url": "https://logo.clearbit.com/cognizant.com",
        "company_tier": 2
    },
    {
        'external_id': 12,
        "company_name": "Capgemini",
        "image_url": "https://logo.clearbit.com/capgemini.com",
        "company_tier": 2
    },

    # ---------- Tier 3 : Startups / New-age Product Companies ----------
    {
        'external_id': 13,
        "company_name": "Zerodha",
        "image_url": "https://logo.clearbit.com/zerodha.com",
        "company_tier": 3
    },
    {
        'external_id': 14,
        "company_name": "Razorpay",
        "image_url": "https://logo.clearbit.com/razorpay.com",
        "company_tier": 3
    },
    {
        'external_id': 15,
        "company_name": "CRED",
        "image_url": "https://logo.clearbit.com/cred.club",
        "company_tier": 3
    },
    {
        'external_id': 16,
        "company_name": "Meesho",
        "image_url": "https://logo.clearbit.com/meesho.com",
        "company_tier": 3
    },
    {
        'external_id': 17,
        "company_name": "Swiggy",
        "image_url": "https://logo.clearbit.com/swiggy.com",
        "company_tier": 3
    },
    {
        'external_id': 18,
        "company_name": "Groww",
        "image_url": "https://logo.clearbit.com/groww.in",
        "company_tier": 3
    },
    {
        'external_id': 19,
        "company_name": "Postman",
        "image_url": "https://logo.clearbit.com/postman.com",
        "company_tier": 3
    },
    {
        'external_id': 20,
        "company_name": "Freshworks",
        "image_url": "https://logo.clearbit.com/freshworks.com",
        "company_tier": 3
    }
]

cs_specializations = [
    "Artificial Intelligence & Machine Learning",
    "Cybersecurity & Information Security",
    "Data Science & Big Data Analytics",
    "Software Engineering & Development",
    "Cloud Computing & DevOps"
]

frontend_specializations_weights = [3, 1, 1, 7, 5]

frontend_titles = [
        "Frontend Development Intern",
        "UI/UX and Frontend Intern",
        "React.js Frontend Intern",
        "Web Development Intern (Frontend)",
        "Frontend Engineer Intern"
    ]

frontend_additional_skills = ['Python', 'MongoDB', 'ML']

# dg_frontend = DataGenerationTemplate(frontend_internships, additional_skills=frontend_additional_skills, companies=cs_companies, specializations=cs_specializations, specialization_weights=frontend_specializations_weights, stream='CS', category='front_end_development')
# dg_frontend.generate_skill_records()
# dg_frontend.generate_internship_records(titles=frontend_titles, start=1)
# dg_frontend.generate_user_records(start=1)
# dg_frontend.generate_selection_records(start=1)

java_backend_internships = [
  {
    "id": 1,
    "required": ["java", "spring_boot", "sql", "hibernate", "rest_api"],
    "must_have": ["java", "spring_boot", "rest_api"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 2,
    "required": ["java", "spring_boot", "sql", "jwt", "validation"],
    "must_have": ["java", "spring_boot", "sql"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 3,
    "required": ["java", "spring", "jdbc", "sql", "logging"],
    "must_have": ["java", "jdbc"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 4,
    "required": ["java", "rest_api", "swagger", "postman"],
    "must_have": ["java", "rest_api"],
    "min_experience": 0,
    "max_experience": 0
  },
  {
    "id": 5,
    "required": ["java", "hibernate", "jpa", "sql"],
    "must_have": ["java", "hibernate"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 6,
    "required": ["java", "spring_boot", "pagination", "filtering"],
    "must_have": ["java", "spring_boot"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 7,
    "required": ["java", "sql", "indexing", "query_optimization"],
    "must_have": ["java", "sql"],
    "min_experience": 0,
    "max_experience": 0
  },
  {
    "id": 8,
    "required": ["java", "spring_boot", "exception_handling"],
    "must_have": ["java", "spring_boot"],
    "min_experience": 0,
    "max_experience": 0
  },
  {
    "id": 9,
    "required": ["java", "spring_boot", "security", "jwt"],
    "must_have": ["java", "spring_boot"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 10,
    "required": ["java", "rest_api", "microservices", "sql"],
    "must_have": ["java", "rest_api"],
    "min_experience": 0,
    "max_experience": 1
  }
]

backend_specialization_weights = [4, 2, 2, 7, 5]

java_backend_titles = [
  "Java Backend Developer Intern",
  "Spring Boot Backend Intern",
  "REST API Developer (Java) Intern",
  "Java Server-Side Development Intern",
  "Backend Software Engineer Intern - Java"
]

java_backend_additional_skills = ['html', 'css', 'Python']

# dg_java_backend = DataGenerationTemplate(internships=java_backend_internships, additional_skills=java_backend_additional_skills, companies=cs_companies, specializations=cs_specializations, specialization_weights=backend_specialization_weights, stream='CS', category='java_back_end_development')
# dg_java_backend.generate_skill_records()
# dg_java_backend.generate_internship_records(titles=java_backend_titles, start=21)
# dg_java_backend.generate_user_records(start=41)
# dg_java_backend.generate_selection_records(start=101)

java_full_stack_internships = [
  {
    "id": 1,
    "required": ["java", "spring_boot", "html", "css", "javascript"],
    "must_have": ["java", "html", "css"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 2,
    "required": ["java", "spring_boot", "react", "rest_api"],
    "must_have": ["java", "spring_boot"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 3,
    "required": ["java", "jsp", "servlets", "jdbc"],
    "must_have": ["java", "servlets"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 4,
    "required": ["java", "spring", "jsp", "sql"],
    "must_have": ["java", "spring"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 5,
    "required": ["java", "html", "css", "javascript", "sql"],
    "must_have": ["java", "html", "css"],
    "min_experience": 0,
    "max_experience": 0
  },
  {
    "id": 6,
    "required": ["java", "spring_boot", "angular", "rest_api"],
    "must_have": ["java", "spring_boot"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 7,
    "required": ["java", "jdbc", "xml", "design_patterns"],
    "must_have": ["java", "jdbc"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 8,
    "required": ["java", "spring_core", "oop", "sql"],
    "must_have": ["java", "oop"],
    "min_experience": 0,
    "max_experience": 0
  },
  {
    "id": 9,
    "required": ["java", "spring", "html", "css"],
    "must_have": ["java", "spring"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 10,
    "required": ["java", "jsp", "servlets", "sql"],
    "must_have": ["java", "jsp"],
    "min_experience": 0,
    "max_experience": 1
  }
]

java_full_stack_specialization_weights = [1, 1, 1, 4, 2]

java_full_stack_titles = [
  "Java Full Stack Developer Intern",
  "Enterprise Java Application Intern",
  "Java Web Development Intern",
  "J2EE Developer Intern",
  "Java + Frontend Integration Intern"
]

java_full_stack_additional_skills = ['Python', 'Posgres', 'Docker']

# dg_java_full_stack = DataGenerationTemplate(internships=java_full_stack_internships, additional_skills=java_full_stack_additional_skills, companies=cs_companies, specializations=cs_specializations, specialization_weights=java_full_stack_specialization_weights, stream = 'CS', category = 'java_full_stack_development')
# dg_java_full_stack.generate_skill_records()
# dg_java_full_stack.generate_internship_records(titles=java_full_stack_titles, start=41)
# dg_java_full_stack.generate_user_records(start=81)
# dg_java_full_stack.generate_selection_records(start = 201)

specialized_java_internships = [
  {
    "id": 21,
    "required": ["java", "microservices", "Docker", "rest_api"],
    "must_have": ["java", "microservices"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 22,
    "required": ["java", "aws", "spring_boot", "Docker"],
    "must_have": ["java", "spring_boot"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 23,
    "required": ["java", "junit", "mockito", "testing"],
    "must_have": ["java", "junit"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 24,
    "required": ["java", "selenium", "automation", "testng"],
    "must_have": ["java", "selenium"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 25,
    "required": ["java", "sql", "spark", "etl"],
    "must_have": ["java", "sql"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 26,
    "required": ["java", "multithreading", "jvm", "profiling"],
    "must_have": ["java", "multithreading"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 27,
    "required": ["java", "Docker", "ci_cd", "deployment"],
    "must_have": ["java", "Docker"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 28,
    "required": ["java", "data_structures", "performance"],
    "must_have": ["java", "data_structures"],
    "min_experience": 0,
    "max_experience": 1
  },
  {
    "id": 29,
    "required": ["java", "sql", "analytics", "reporting"],
    "must_have": ["java", "sql"],
    "min_experience": 0,
    "max_experience": 0
  },
  {
    "id": 30,
    "required": ["java", "spring_boot", "system_design"],
    "must_have": ["java", "spring_boot"],
    "min_experience": 0,
    "max_experience": 1
  }
]

specialized_java_internship_titles = [
  "Java Microservices Intern",
  "Java Cloud Development Intern",
  "Java Automation & Testing Intern",
  "Java Data Engineering Intern",
  "Java DevOps Intern"
]

specialized_java_internships_weights = [1, 1, 1, 6, 3]

dg_specialized_internships = DataGenerationTemplate(internships=specialized_java_internships, additional_skills = java_backend_additional_skills, companies=cs_companies, specializations = cs_specializations, specialization_weights=specialized_java_internships_weights, stream='CS', category='java_specialized_internships')
# dg_specialized_internships.generate_skill_records()
dg_specialized_internships.generate_internship_records(titles=specialized_java_internship_titles, start=61)
dg_specialized_internships.generate_user_records(start=121)
dg_specialized_internships.generate_selection_records(start=301)


