
const BASE_URL = import.meta.env.VITE_API_URL

export type Params = Record<string, string | number | boolean | undefined | null>;

export function buildApiUrl(path: string, params?: Params) {
    console.log(BASE_URL);
    const url = new URL(path, BASE_URL);
    console.log(url.toString());
    if (params) {
        for (const [key, value] of Object.entries(params)) {
            if (value !== undefined && value !== null && value !== '') {
                url.searchParams.set(key, value.toString());
            }
        }
    }
    return url.toString();
}