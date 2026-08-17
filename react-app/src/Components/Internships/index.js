import "./index.css";
import { Component } from "react";
import { ThreeDots } from "react-loader-spinner";
import { BsSearch } from "react-icons/bs";
import InternshipCard from '../InternshipCard'

const profileApiStatusConstants = {
  initial: "INITIAL",
  success: "SUCCESS",
  failure: "FAILURE",
  loading: "LOADING",
};

const jobsApiStatusConstants = {
  initial: "INITIAL",
  success: "SUCCESS",
  failure: "FAILURE",
  loading: "LOADING",
};

const rolesList = [
  { roleId: "FRONTEND", label: "Frontend Developer" },
  { roleId: "BACKEND", label: "Backend Developer" },
  { roleId: "FULLSTACK", label: "Full Stack Developer" },
  { roleId: "DATASCI", label: "Data Scientist" },
  { roleId: "MLENG", label: "AI/ML Engineer" },
  { roleId: "MOBILE", label: "Mobile App Developer" },
  { roleId: "UIUX", label: "UI/UX Designer" },
  { roleId: "CLOUD", label: "Cloud Engineer" },
  { roleId: "CYBER", label: "Cybersecurity Analyst" },
];

const sectorsList = [
  { sectorId: "AGR", label: "Agriculture" },
  { sectorId: "HLTH", label: "Healthcare / Medical" },
  { sectorId: "EDU", label: "Education" },
  { sectorId: "IT", label: "IT / Software" },
  { sectorId: "FIN", label: "Finance" },
  { sectorId: "MFG", label: "Manufacturing" },
  { sectorId: "NRG", label: "Energy" },
  { sectorId: "GOV", label: "Government / Public Policy" },
  { sectorId: "NGO", label: "Social Work / NGO" },
];

const employmentModesList = [
  'Onsite', 'Remote', 'Hybrid', 'Any'
]

class Jobs extends Component {
  state = {
    selectedUserId: "",
    locationsList: [],
    selectedLocation: "",
    selectedRole: '',
    selectedMode: '',
    userDetails: null,
    userApiStatus: profileApiStatusConstants.initial,
    jobsList: [],
    jobsApiStatus: jobsApiStatusConstants.initial,
    usersList: []
  };

  componentDidMount() {
    this.getJobsList();
    this.getLocationsList();
    this.getUsersList();
  }

  getUsersList = async () => {
    const url = "/users/"
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json();
      console.log(data)
      const usersList = data.map((item) => ({
        label: item.name,
        userId: item.id
      }))
      this.setState({usersList: usersList})
    }
  }

  getLocationsList = async () => {
    const url = "/locations/";
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json();
      const locations = data.map((item) => ({
        label: item.name,
        locationId: item.id
      }))
      this.setState({locationsList: locations})
    }
    
  }

  // Jobs API
  getJobsList = async () => {
    this.setState({ jobsApiStatus: jobsApiStatusConstants.loading });
    const { selectedUserId } = this.state;

    // Decide API based on whether user is selected
    const url = selectedUserId
      ? `/internships/filtered/${selectedUserId}`
      : `/internships`;

    try {
      let requestBody;
      const {selectedMode, selectedLocation} = this.state;
      if (selectedUserId) {
        requestBody = {
          user_requirements: {
            preferred_mode: selectedMode,
            preferred_state: selectedLocation
          },
          project_experience_description: {
            project_description: '',
            experience_description: ''
          }
        }
      
      } else {
        requestBody = {}
      }
      const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestBody),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("Jobs API response:", data);

        const formatted = data.map((each) => ({
          id: each.internshipId,
          title: each.title,
          companyLogoUrl: each.company.imageUrl,
          companyName: each.company.companyName,
          location: `${each.district}, ${each.state}`,
          sector: each.sector,
          jobDescription: each.description,
          duration: each.duration,
          postingTime: each.postingTime,
          appliedCount: each.appliedCount,
          totalCount: each.totalCount,
          benefits: each.benifits,
        }));
        console.log("Formatted job data", formatted)
        this.setState({
          jobsList: formatted,
          jobsApiStatus: jobsApiStatusConstants.success,
        });
      } else {
        this.setState({ jobsApiStatus: jobsApiStatusConstants.failure });
      }
    } catch (error) {
      console.error("Error fetching jobs:", error);
      this.setState({ jobsApiStatus: jobsApiStatusConstants.failure });
    }
  };

  // User API
  getUserDetails = async (userId) => {
    this.setState({ userApiStatus: profileApiStatusConstants.loading });
    const url = `/users/${userId}`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        const formatted = {
          name: data.name,
          location: data.location,
          gender: data.gender,
        };
        console.log(formatted);
        this.setState({
          userDetails: formatted,
          userApiStatus: profileApiStatusConstants.success,
        });
      } else {
        console.log("running else block");
        this.setState({ userApiStatus: profileApiStatusConstants.failure });
      }
    } catch {
      this.setState({ userApiStatus: profileApiStatusConstants.failure });
    }
  };

  onChangeUser = (event) => {
    const userId = event.target.value;
    this.setState({ selectedUserId: userId }, () => {
      if (userId) {
        this.getUserDetails(userId);
      } else {
        this.getJobsList(); // reset to all internships if no user
      }
    });
  };

  onRetryUser = () => {
    const { selectedUserId } = this.state;
    if (selectedUserId) {
      this.getUserDetails(selectedUserId);
    }
  };

  onRetryJobs = () => this.getJobsList();

  // User dropdown & card renderer
  renderUserSection = () => {
    const { selectedUserId, userDetails, userApiStatus, usersList } = this.state;

    switch (userApiStatus) {
      case profileApiStatusConstants.initial:
        return (
          <div className="filter-container">
            <h1 className="filter-title">Select User</h1>
            <select
              className="filter-dropdown"
              value={selectedUserId}
              onChange={this.onChangeUser}
            >
              <option value="">Choose a User</option>
              {usersList.map((user) => (
                <option key={user.userId} value={user.userId}>
                  {user.label}
                </option>
              ))}
            </select>
          </div>
        );

      case profileApiStatusConstants.loading:
        return (
          <div className="loader-bg" data-testid="loader">
            <ThreeDots
              height={50}
              width={50}
              color="#ffffff"
              ariaLabel="three-dots-loading"
              visible={true}
            />
          </div>
        );

      case profileApiStatusConstants.success:
        return (
          <div className="profile-card">
            <h2>Name: {userDetails.name}</h2>
            <p>Location: {userDetails.location}</p>
            <p>Gender: {userDetails.gender}</p>
          </div>
        );

      case profileApiStatusConstants.failure:
        return (
          <button
            className="retry-button"
            type="button"
            onClick={this.onRetryUser}
          >
            Retry
          </button>
        );

      default:
        return null;
    }
  };

  renderJobsSection = () => {
    const { jobsList, jobsApiStatus } = this.state;

    switch (jobsApiStatus) {
      case jobsApiStatusConstants.loading:
        return (
          <div className="loader-bg" data-testid="loader">
            <ThreeDots height={50} width={50} color="#ffffff" />
          </div>
        );
      case jobsApiStatusConstants.success:
        if (jobsList.length === 0) {
          return (
            <div className="loader-bg">
              <img
                src="https://assets.ccbp.in/frontend/react-js/no-jobs-img.png"
                alt="no jobs"
                className="failure-view-img"
              />
              <h1 className="failure-view-title">No Jobs Found</h1>
              <p className="failure-view-description">
                We could not find any jobs. Try other filters.
              </p>
            </div>
          );
        }

        return (
          <ul className="jobs-list">
            {jobsList.map((job) => (
              <InternshipCard job={job}/>
            ))}
          </ul>
        );
      case jobsApiStatusConstants.failure:
        return (
          <div className="loader-bg">
            <img
              src="https://assets.ccbp.in/frontend/react-js/failure-img.png"
              alt="failure view"
              className="failure-view-img"
            />
            <h1 className="failure-view-title">Oops! Something Went Wrong</h1>
            <p className="failure-view-description">
              We cannot seem to find the page you are looking for
            </p>
            <button
              type="button"
              className="retry-button"
              onClick={this.onRetryJobs}
            >
              Retry
            </button>
          </div>
        );
      default:
        return null;
    }
  };

  onChangeLocation = (event) => {
    this.setState({selectedLocation: event.target.value})
  }

  onChangeRole = (event) => {
    this.setState({selectedRole: event.target.value})
  }

  onChangeEmploymentMode = (event) => {
    this.setState({selectedMode: event.target.value})
  }

  render() {
    const {locationsList, selectedLocation, selectedRole, selectedMode} = this.state
    return (
      <div className="jobs-page">
        <div className="search-container-sm">
          <input
            type="search"
            className="search-input"
            placeholder="Search"
            onChange={this.onSearch}
          />
          <button
            className="search-icon"
            type="button"
            data-testid="searchButton"
            onClick={this.getJobsList}
          >
            <BsSearch />
          </button>
        </div>

        <div className="jobs-page-left-panel">
          {/* user dropdown/card */}
          {this.renderUserSection()}

          {/* Role */}
          <div className="filter-container">
            <div className="label-icon-container">
              <h1 className="filter-title">Role</h1>
            </div>
            <select
              className="filter-dropdown"
              name="role"
              value={selectedRole}
              onChange={this.onChangeRole}
            >
              <option value="">Select Role</option>
              {rolesList.map((role) => (
                <option key={role.roleId} value={role.roleId}>
                  {role.label}
                </option>
              ))}
            </select>
          </div>
          <hr />

          {/* Sector */}
          <div className="filter-container">
            <div className="label-icon-container">
              <h1 className="filter-title">Sector</h1>
            </div>
            <select
              className="filter-dropdown"
              name="sector"
              onChange={this.onChangeSector}
            >
              <option value="">Select Sector</option>
              {sectorsList.map((sector) => (
                <option key={sector.sectorId} value={sector.sectorId}>
                  {sector.label}
                </option>
              ))}
            </select>
          </div>
          <hr />

          {/* Preferred Location */}
          <div className="filter-container">
            <div className="label-icon-container">
              <h1 className="filter-title">Preferred Location</h1>
            </div>
            <select
              className="filter-dropdown"
              name="location"
              onChange={this.onChangeLocation}
              value={selectedLocation}
            >
              <option value="">Select Location</option>
              {locationsList.map((location) => (
                <option key={location.locationId} value={location.label}>
                  {location.label}
                </option>
              ))}
            </select>
          </div>
          <hr />

          {/* Mode of Employment */}

          <div className="filter-container">
            <div className="label-icon-container">
              <h1 className="filter-title">Mode of Employment</h1>
            </div>
            <ul className="filter-list-container">
              {employmentModesList.map((mode) => (
                <li key={mode}>
                  <input
                    className="filter-checkbox"
                    type="radio"
                    id={mode}
                    value={mode}
                    name="mode"
                    onChange={this.onChangeEmploymentMode}
                  />
                  <label
                    htmlFor={mode}
                    className="filter-label"
                  >
                    {mode}
                  </label>
                </li>
              ))}
            </ul>
          </div>
          <hr />

          <button
            type="button"
            className="retry-button"
            onClick={this.onRetryJobs}
          >
            Find Jobs
          </button>
        </div>

        <div className="jobs-page-right-panel">
          <div className="search-container">
            <input
              type="search"
              className="search-input"
              placeholder="Search"
              onChange={this.onSearch}
            />
            <button
              className="search-icon"
              type="button"
              data-testid="searchButton"
              onClick={this.getJobsList}
            >
              <BsSearch />
            </button>
          </div>
          {this.renderJobsSection()}
        </div>
      </div>
    );
  }
}

export default Jobs;
