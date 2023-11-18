import { PartialRating, Rating, RatingType } from "./rating";

export enum ProjectState {
    DRAFT = "DRAFT",
    SUBMITTED = "SUBMITTED",
    ACCEPTED = "ACCEPTED",
    ACCEPTED_INDIVIDUAL = "ACCEPTED_INDIVIDUAL",
    ACCEPTED_INITIAL = "ACCEPTED_INITIAL",
    ACCEPTED_FINAL = "ACCEPTED_FINAL",
    REJECTED = "REJECTED",
    UNRECOGNIZED = "UNRECOGNIZED",
}

export interface Assessor {
    assessorId: number;
    personId: number;
    firstName: string;
    lastName: string;
    email: string;
}

export interface ApplicationReport {
    isDraft: boolean;
    reportSubmissionDate?: string;
    projectGoals?: string;
    organisationStructure?: string;
    divisionOfWork?: string;
    projectSchedule?: string;
    attachements?: string;
}

export interface SubmissionDTO {
    submissionId: number;
    name: string;
    durationDays: string;
    assessors: Assessor[];
    ratings: Rating[];
    year: number;
}

export interface SubmissionDetailsDTO {
    submissionId: number;
    name: string;
    teamSize: number;
    finishDate: Date;
    budget: string;
    description: string;
    appReportDto?: ApplicationReport;
    points: number;
    status: ProjectState;
}

export interface AssessorsRatings {
    assessorId: number;
    ratingId: number;
    firstName: string;
    lastName: string;
    draft: boolean;
    partialRatings: PartialRating[];
}

export interface UpdateSubmissionBody {
    criterionId?: number;
    ratingId?: number;
    partialRatingId?: number;
    justification: string;
    points: number;
}

export interface AddRatingBody {
    ratingType: RatingType;
}
