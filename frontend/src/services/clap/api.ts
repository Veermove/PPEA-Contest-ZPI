import { buildAuthorizationHeader as buildDefaultHeaders } from "../util";
import { clapConfig } from "./config";
import { PartialRating, Rating, RatingsDTO } from "./model/rating";
import { AddRatingBody as NewRatingBody, SubmissionDTO, SubmissionDetailsDTO, UpdateSubmissionBody } from "./model/submission";

export class ClapApi {
  private readonly defaultHeaders: HeadersInit;
  private readonly baseUrl = clapConfig.baseUrl;
  private cache: Record<string, any> = {};

  constructor(idToken: string) {
    this.defaultHeaders = buildDefaultHeaders(idToken);
  }

  private static async  handleResponse(response: Response): Promise<any> {
    const json = await response.json();
    if (!response.ok) {
      throw new Error(json);
    }
    return json;
  }

  async getSubmissions(): Promise<SubmissionDTO[]> {
    if (this.cache.submissions) {
      return this.cache.submissions;
    }
    const response = await fetch(`${this.baseUrl}/submissions`, {
      headers: this.defaultHeaders
    });
    const submissions = await ClapApi.handleResponse(response);
    this.cache.submissions = submissions;
    return submissions;
  }

  async getSubmissionDetails(submissionId: number): Promise<SubmissionDetailsDTO> {
    const response = await fetch(`${this.baseUrl}/submissions/${submissionId}`, {
      headers: this.defaultHeaders
    });
    return await ClapApi.handleResponse(response);
  }

  async getSubmissionRatings(submissionId: number): Promise<RatingsDTO> {
    const response = await fetch(`${this.baseUrl}/ratings/${submissionId}`, {
      headers: this.defaultHeaders
    });
    return await ClapApi.handleResponse(response);
  }

  async createRating(submissionId: number, newRatingBody: NewRatingBody): Promise<Rating> {
    const response = await fetch(`${this.baseUrl}/ratings/${submissionId}`, {
      method: "POST",
      headers: this.defaultHeaders,
      body: JSON.stringify(newRatingBody)
    });
    return await ClapApi.handleResponse(response);
  }

  async submitRatingDraft(ratingId: number): Promise<Rating> {
    const response = await fetch(`${this.baseUrl}/ratings/${ratingId}`, {
      method: "PUT",
      headers: this.defaultHeaders
    });
    return await ClapApi.handleResponse(response);
  }

  async upsertPartialRating(updateSubmissionBody: UpdateSubmissionBody): Promise<PartialRating> {
    const response = await fetch(`${this.baseUrl}/partialratings`, {
      method: "POST",
      headers: this.defaultHeaders,
      body: JSON.stringify(updateSubmissionBody)
    });
    return await ClapApi.handleResponse(response);
  }
}

