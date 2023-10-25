import { buildAuthorizationHeader as buildDefaultHeaders } from "../util";
import { RatingsDTO } from "./model/rating";
import { SubmissionDTO, SubmissionDetailsDTO } from "./model/submission";

export class ClapApi {
    private readonly defaultHeaders: HeadersInit;

    constructor(readonly baseUrl: string, idToken: string) { 
        this.defaultHeaders = buildDefaultHeaders(idToken);
    }

    async getSubmissions(assesorId: number): Promise<SubmissionDTO[]> {
        const stringParams = new URLSearchParams({ assesorId: assesorId.toString() });
        const response = await fetch(`${this.baseUrl}/submissions?${stringParams.toString()}`, {
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
        const response = await fetch(`${this.baseUrl}/submissions/${submissionId}/ratings`, {
            headers: this.defaultHeaders
        });
        return await response.json();
    }
}
