import { useState, useCallback } from 'react';
import { InteractionType, PopupRequest, AuthenticationResult, EventType, AccountInfo } from '@azure/msal-browser';
import { useMsal, useMsalAuthentication } from '@azure/msal-react';

interface FetchWithMsalReturn {
  isLoading: boolean;
  error: Error | null;
  data: any;
  execute: (method: string, endpoint: string, data?: any) => Promise<any>;
}

/**
 * Custom hook to call a web API using bearer token obtained from MSAL
 * @param {PopupRequest} msalRequest
 * @returns
 */
const useFetchWithMsal = (msalRequest: PopupRequest): FetchWithMsalReturn => {
  const { instance } = useMsal();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);
  const [data, setData] = useState<any>(null);

  const { result, error: msalError } = useMsalAuthentication(InteractionType.Popup, {
    ...msalRequest,
    account: instance.getActiveAccount() as AccountInfo,
    redirectUri: '/',
  });

  /**
   * Execute a fetch request with the given options
   * @param {string} method: GET, POST, PUT, DELETE
   * @param {String} endpoint: The endpoint to call
   * @param {Object} data: The data to send to the endpoint, if any
   * @returns JSON response
   */
  const execute = async (method: string, endpoint: string, data: any = null): Promise<any> => {
    console.log('execute : ', msalError);
    if (msalError) {
      setError(msalError);
      return;
    }

    if (result) {
      try {
        let response: Response | null = null;

        const headers = new Headers();
        const bearer = `Bearer ${(result as AuthenticationResult).accessToken}`;
        console.log('bearer : ', bearer);
        headers.append('Authorization', bearer);

        if (data) headers.append('Content-Type', 'application/json');

        let options: RequestInit = {
          method: method,
          headers: headers,
          body: data ? JSON.stringify(data) : null,
        };

        setIsLoading(true);
        response = await fetch(endpoint, options);

        if (response.status === 200 || response.status === 201) {
          let responseData: any = response;

          try {
            responseData = await response.json();
          } catch (error) {
            console.log(error);
          } finally {
            setData(responseData);
            setIsLoading(false);
            return responseData;
          }
        }

        setIsLoading(false);
        return response;
      } catch (e) {
        setError(e as Error);
        setIsLoading(false);
        throw e;
      }
    }
  };

  return {
    isLoading,
    error,
    data,
    execute: useCallback(execute, [result, msalError]), // to avoid infinite calls when inside a `useEffect`
  };
};

export default useFetchWithMsal;
