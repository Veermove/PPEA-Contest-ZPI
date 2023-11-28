import { buildAuthorizationHeader as buildDefaultHeaders } from "../util";
import { clapConfig } from "./config";
import { PartialRating, Rating, RatingsDTO } from "./model/rating";
import { StudyVisit } from "./model/studyVisits";
import { AddRatingBody as NewRatingBody, SubmissionDTO, SubmissionDetailsDTO, UpsertPartialRatingBody } from "./model/submission";

export class ClapApi {
  private readonly defaultHeaders: HeadersInit;
  private readonly baseUrl = clapConfig.baseUrl;
  private cache: Record<string, any> = {};

  constructor(idToken: string) {
    this.defaultHeaders = buildDefaultHeaders(idToken);
  }

  private static async handleResponse(response: Response): Promise<any> {
    if (!response.ok) {
      throw Error(`${response.status}: ${await response.text()}`)
    }
    return await response.json();
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

  async getStudyVisit(submissionId: number): Promise<StudyVisit> {
    const response = await fetch(`${this.baseUrl}/submissions/${submissionId}/visits`, {
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

  async upsertPartialRating(upsertPartialRatingBody: UpsertPartialRatingBody): Promise<PartialRating> {
    const response = await fetch(`${this.baseUrl}/partialratings`, {
      method: "POST",
      headers: this.defaultHeaders,
      body: JSON.stringify(upsertPartialRatingBody)
    });
    return await ClapApi.handleResponse(response);
  }
}

