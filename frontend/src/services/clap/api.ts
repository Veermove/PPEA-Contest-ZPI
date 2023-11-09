import { buildAuthorizationHeader as buildDefaultHeaders } from "../util";
import { clapConfig } from "./config";
import { PartialRating, Rating, RatingsDTO } from "./model/rating";
import { AddRatingBody as NewRatingBody, SubmissionDTO, SubmissionDetailsDTO, UpdateSubmissionBody } from "./model/submission";

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

  async createRating(submissionId: number, newRatingBody: NewRatingBody): Promise<Rating> {
    const response = await fetch(`${this.baseUrl}/ratings/${submissionId}`, {
      method: "POST",
      headers: this.defaultHeaders,
      body: JSON.stringify(newRatingBody)
    });
    return await response.json();
  }

  async submitRatingDraft(ratingId: number): Promise<Rating> {
    const response = await fetch(`${this.baseUrl}/ratings/${ratingId}`, {
      method: "PUT",
      headers: this.defaultHeaders
    });
    return await response.json();
  }

  async upsertPartialRating(updateSubmissionBody: UpdateSubmissionBody): Promise<PartialRating> {
    const response = await fetch(`${this.baseUrl}/partialRatings`, {
      method: "POST",
      headers: this.defaultHeaders,
      body: JSON.stringify(updateSubmissionBody)
    });
    return await response.json();
  }
}

