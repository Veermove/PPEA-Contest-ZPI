export function buildAuthorizationHeader(idToken: string) {
    return { Authorization: `Bearer ${idToken}` };
}
