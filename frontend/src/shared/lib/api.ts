import { buildApiUrl, type Params } from "./util";

export class ValidationError extends Error {
    errors: Record<string, string>;

    constructor(errors: Record<string, string>, message = "Erro de validação") {
        super(message);
        this.name = "ValidationError";
        this.errors = errors;
        
    }
}

export class ApiError extends Error {
    status: number;
    message: string;

    constructor(status: number, message: string) {
        super(message);
        this.status = status;
        this.message = message;
    }
}

async function apiRequest<T>(method: string, path: string, options: { body?: unknown; params?: Params; signal?: AbortSignal } = {},): Promise<T> {
    const response = await fetch(buildApiUrl(path, options.params),  {
        method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: options.body ? JSON.stringify(options.body) : undefined,
        signal: options.signal,
    });

    if(!response.ok) {
        const error = await response.json().catch(() => ({ message: 'Unknown error' }));
        throw new ApiError(response.status, error.message);
    }

    if (response.status === 204) return undefined as T;

    const text = await response.text();
    if (!text) return undefined as T;
    return JSON.parse(text) as T;
} 

export const api = {
    get: <T>(path: string, params?: Params, signal?: AbortSignal) => apiRequest<T>('GET', path, { params, signal }),
    post: <T>(path: string, body?: unknown, signal?: AbortSignal) => apiRequest<T>('POST', path, { body, signal }),
    put: <T>(path: string, body?: unknown, signal?: AbortSignal) => apiRequest<T>('PUT', path, { body, signal }),
    delete: <T>(path: string, signal?: AbortSignal) => apiRequest<T>('DELETE', path, { signal }),
}