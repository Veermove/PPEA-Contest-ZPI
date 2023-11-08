import { buildAuthorizationHeader as buildDefaultHeaders } from "../util";
import { clapConfig } from "./config";
import { PartialRating, RatingsDTO } from "./model/rating";
import { AddRatingBody, SubmissionDTO, SubmissionDetailsDTO, UpdateSubmissionBody } from "./model/submission";

export class ClapApi {
  private readonly defaultHeaders: HeadersInit;
  private readonly baseUrl = clapConfig.baseUrl;

  constructor(idToken: string) {
    this.defaultHeaders = buildDefaultHeaders(idToken);
  }

  async getSubmissions(): Promise<SubmissionDTO[]> {
    const response = await fetch(`${this.baseUrl}/submissions`, {
      headers: this.defaultHeaders
    });
    return await response.json();
  }

  async getSubmissionDetails(submissionId: number): Promise<SubmissionDetailsDTO> {
    const response = await fetch(`${this.baseUrl}/submissions/${submissionId}`, {
      headers: this.defaultHeaders
    });
    return await response.json();
  }

  async getSubmissionRatings(submissionId: number): Promise<RatingsDTO> {
    const response = await fetch(`${this.baseUrl}/ratings/${submissionId}`, {
      headers: this.defaultHeaders
    });
    return await response.json();
  }

  // TODO to be aligned with actual CLAP contract
  async updateRating(partialRatingId: number, updateSubmissionBody: UpdateSubmissionBody): Promise<PartialRating> {
    const response = await fetch(`${this.baseUrl}/ratings/${partialRatingId}`, {
      method: "POST",
      headers: this.defaultHeaders,
      body: JSON.stringify(updateSubmissionBody)
    });
    return await response.json();
  }

  async addRating(submissionId: number, addRatingBody: AddRatingBody) {
    const response = await fetch(`${this.baseUrl}/submissions/ratings/${submissionId}`, {
      method: "PUT",
      headers: this.defaultHeaders,
      body: JSON.stringify(addRatingBody)
    });
    return await response.json();
  }
}
