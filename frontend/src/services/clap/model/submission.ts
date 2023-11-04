import { PartialRating, Rating } from "./rating";

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
}

export interface AssessorsRatings {
    firstName: string;
    lastName: string;
    partialRatings: PartialRating[];
}

