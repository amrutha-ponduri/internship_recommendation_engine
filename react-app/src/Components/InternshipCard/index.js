import {Component} from 'react'
import {Link} from 'react-router-dom'
import { BsCheck2Circle } from "react-icons/bs";
import { MdOutlineTimer, MdLocationOn } from "react-icons/md";
import './index.css'

class InternshipCard extends Component {
    
    render() {
        const {job} = this.props
        return <Link
                to={`/jobs/${job.id}`}
                className="link"
                state={{ extraDetails: job }}
                key={job.id}
              >
                <li className="jobs-list-item">
                  <div className="job-header">
                    <img
                      src="https://res.cloudinary.com/dcbw1me25/image/upload/v1786887445/creative-logo-design-for-real-estate-company-vector_b0m6kc.jpg"
                      alt={job.companyName}
                      className="company-logo"
                    />
                    <div>
                      <h1 className="job-title">{job.title}</h1>
                      <p className="sector">{job.sector}</p>
                    </div>
                  </div>

                  <div className="job-details-and-salary">
                    <div className="job-details">
                      <div className="icon-text-container">
                        <MdLocationOn />
                        <p className="job-location">{job.location}</p>
                      </div>
                      <div className="icon-text-container no-wrap">
                        <MdOutlineTimer />
                        <p className="job-duration">{job.duration}</p>
                      </div>
                    </div>
                    <div className="icon-text-container">
                      <BsCheck2Circle />
                      <p className="job-application">
                        {job.appliedCount}/{job.totalCount} applied
                      </p>
                    </div>
                  </div>
                  <hr />

                  <h1 className="job-details-section-title">Description</h1>
                  <p className="job-description">{job.jobDescription}</p>

                  {job.benefits && (
                    <>
                      <h1 className="job-details-section-title">Benefits</h1>
                      <p className="job-description">{job.benefits}</p>
                    </>
                  )}
                </li>
              </Link>
    }
}

export default InternshipCard