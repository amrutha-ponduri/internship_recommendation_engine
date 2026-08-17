import "./index.css";
import { Component } from "react";
import Cookies from "js-cookie";
import { ThreeDots } from "react-loader-spinner";
import { MdLocationOn, MdSchool } from "react-icons/md";
import { BsFillCheckCircleFill } from "react-icons/bs";
import { MdOutlineTimer } from "react-icons/md";
import { useParams, useLocation } from "react-router-dom";

const apiStatusConstants = {
  initial: "INITIAL",
  success: "SUCCESS",
  failure: "FAILURE",
  loading: "LOADING",
};

class InternshipItemDetailsWrapper extends Component {
  state = {
    jobDetails: this.props.extraDetails || {},
    responseStatus: apiStatusConstants.initial,
  };

  componentDidMount() {
    this.getJobDetails();
  }

  getJobDetails = async () => {
    const { id } = this.props;
    this.setState({ responseStatus: apiStatusConstants.loading });
    const url = `/internship/details/${id}`;
    const jwtToken = Cookies.get("jwt_token");
    const options = {
      headers: jwtToken ? { Authorization: `Bearer ${jwtToken}` } : {},
      method: "GET",
    };

    try {
      const response = await fetch(url, options);
      if (response.ok) {
        const data = await response.json();
        // merge backend data with existing props (extraDetails)
        this.setState({
          jobDetails: { ...this.state.jobDetails, ...data },
          responseStatus: apiStatusConstants.success,
        });
      } else {
        this.setState({ responseStatus: apiStatusConstants.failure });
      }
    } catch {
      this.setState({ responseStatus: apiStatusConstants.failure });
    }
  };

  onRetry = () => this.getJobDetails();

  renderJobDetails = () => {
    const { responseStatus, jobDetails } = this.state;

    switch (responseStatus) {
      case apiStatusConstants.success:
        return (
          <div className="job-item-details-page">
            <div className="job-item-details-container">
              {/* Header */}
              <div className="job-header">
                <img
                  src="https://res.cloudinary.com/dcbw1me25/image/upload/v1786887445/creative-logo-design-for-real-estate-company-vector_b0m6kc.jpg"
                  alt={jobDetails.companyName}
                  className="company-logo"
                />
                <div>
                  <h1 className="job-title">{jobDetails.title}</h1>
                  <p className="sector">{jobDetails.sector}</p>
                </div>
              </div>

              {/* Details */}
              <div className="job-details-and-salary">
                <div className="job-details">
                  <div className="icon-text-container">
                    <MdLocationOn />
                    <p className="job-location">{jobDetails.location}</p>
                  </div>
                  <div className="icon-text-container">
                    <MdOutlineTimer />
                    <p className="job-duration no-wrap">
                      {jobDetails.duration}
                    </p>
                  </div>
                </div>
                <div className="icon-text-container">
                  <BsFillCheckCircleFill />
                  <p className="job-application">
                    {jobDetails.appliedCount}/{jobDetails.totalCount} applied
                  </p>
                </div>
              </div>

              <hr />

              {/* Backend fields */}
              <h2 className="job-details-section-subtitle">Description</h2>
              <p className="job-description">
                {jobDetails.internshipDescription}
              </p>

              {jobDetails.qualificationsDescription && (
                <>
                  <h2 className="job-details-section-subtitle">
                    Qualifications
                  </h2>
                  <p className="job-description">
                    {jobDetails.qualificationsDescription}
                  </p>
                </>
              )}

              {jobDetails.skills?.length > 0 && (
                <>
                  <h2 className="job-details-section-subtitle">Skills</h2>
                  <ul className="skills-list-container">
                    {jobDetails.skills.map((skill) => (
                      <li key={skill.id} className="skills-list-item">
                        {/* <img
                          src={skill.imageURL}
                          alt={skill.skillName}
                          className="skill-logo"
                        /> */}
                        <p className="skill-name">{skill.skillName}</p>
                      </li>
                    ))}
                  </ul>
                </>
              )}

              {jobDetails.benefits && (
                <>
                  <h2 className="job-details-section-subtitle">Benefits</h2>
                  <p className="job-description">{jobDetails.benefits}</p>
                </>
              )}

              <h2
                className="job-details-section-subtitle"
                style={{ marginBottom: "0px" }}
              >
                Other Details
              </h2>
              <div className="icon-text-container">
                <MdSchool className="icon" />
                <p className="job-field">
                  {jobDetails.field} | {jobDetails.minimumQualification} |{" "}
                  {jobDetails.course} | {jobDetails.specialization}
                </p>
              </div>
              <button className="apply-button">Apply Now</button>
            </div>
          </div>
        );

      case apiStatusConstants.loading:
        return (
          <div className="loader-bg" data-testid="loader">
            <ThreeDots height={50} width={50} color="#ffffff" visible={true} />
          </div>
        );

      case apiStatusConstants.failure:
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
              onClick={this.onRetry}
            >
              Retry
            </button>
          </div>
        );

      default:
        return null;
    }
  };

  render() {
    return <>{this.renderJobDetails()}</>;
  }
}

function InternshipItemDetails() {
  const { id } = useParams();
  const location = useLocation();
  const jobFromCard = location.state?.extraDetails;

  return <InternshipItemDetailsWrapper extraDetails={jobFromCard} id={id} />;
}

export default InternshipItemDetails;
