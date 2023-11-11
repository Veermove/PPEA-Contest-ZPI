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

  private static handleResponse(response: Response): Response {
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    return response;
  }

  async getSubmissions(): Promise<SubmissionDTO[]> {
    const response = await fetch(`${this.baseUrl}/submissions`, {
      headers: this.defaultHeaders
    });
    return await ClapApi.handleResponse(response).json();
  }

  async getSubmissionDetails(submissionId: number): Promise<SubmissionDetailsDTO> {
    const response = await fetch(`${this.baseUrl}/submissions/${submissionId}`, {
      headers: this.defaultHeaders
    });
    return await ClapApi.handleResponse(response).json();
  }

  async getSubmissionRatings(submissionId: number): Promise<RatingsDTO> {
    const response = await fetch(`${this.baseUrl}/ratings/${submissionId}`, {
      headers: this.defaultHeaders
    });
    return await ClapApi.handleResponse(response).json();
  }

  async createRating(submissionId: number, newRatingBody: NewRatingBody): Promise<Rating> {
    const response = await fetch(`${this.baseUrl}/ratings/${submissionId}`, {
      method: "POST",
      headers: this.defaultHeaders,
      body: JSON.stringify(newRatingBody)
    });
    return await ClapApi.handleResponse(response).json();
  }

  async submitRatingDraft(ratingId: number): Promise<Rating> {
    const response = await fetch(`${this.baseUrl}/ratings/${ratingId}`, {
      method: "PUT",
      headers: this.defaultHeaders
    });
    return await ClapApi.handleResponse(response).json();
  }

  async upsertPartialRating(updateSubmissionBody: UpdateSubmissionBody): Promise<PartialRating> {
    const response = await fetch(`${this.baseUrl}/partialratings`, {
      method: "POST",
      headers: this.defaultHeaders,
      body: JSON.stringify(updateSubmissionBody)
    });
    return await ClapApi.handleResponse(response).json();
  }
}

