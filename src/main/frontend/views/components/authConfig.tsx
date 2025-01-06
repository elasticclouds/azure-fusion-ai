import { LogLevel } from '@azure/msal-browser';

interface MsalConfig {
  auth: {
    clientId: string;
    authority: string;
    redirectUri: string;
    postLogoutRedirectUri: string;
    navigateToLoginRequestUrl: boolean;
  };
  cache: {
    cacheLocation: 'localStorage' | 'sessionStorage';
    storeAuthStateInCookie: boolean;
  };
  system: {
    loggerOptions: {
      loggerCallback: (level: LogLevel, message: string, containsPii: boolean) => void;
    };
  };
}

interface ProtectedResources {
  chatAPI: {
    endpoint: string;
    scopes: {
      read: string[];
      write: string[];
    };
  };
}

interface LoginRequest {
  scopes: string[];
}

export const msalConfig: MsalConfig = {
  auth: {
    clientId: 'fccca4d3-5c29-410e-8fd7-409d414c9bb0', // This is the ONLY mandatory field that you need to supply.
    authority: 'https://login.microsoftonline.com/4d0cfe16-c0fe-4f01-8697-16a317115f60/', // Replace the placeholder with your tenant subdomain
    redirectUri: '/', // You must register this URI on Microsoft Entra admin center/App Registration. Defaults to window.location.origin
    postLogoutRedirectUri: '/', // Indicates the page to navigate after logout.
    navigateToLoginRequestUrl: false, // If "true", will navigate back to the original request location before processing the auth code response. If "false", will navigate to the app's root route.
  },
  cache: {
    cacheLocation: 'localStorage', // Configures cache location. "sessionStorage" is more secure, but "localStorage" gives you SSO between tabs.
    storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
  },
  system: {
    loggerOptions: {
      loggerCallback: (level: LogLevel, message: string, containsPii: boolean): void => {
        if (containsPii) {
          return;
        }
        // Function to get current time in IST
        const getISTTime = () => {
          const now = new Date();
          const utcOffset = now.getTime() + now.getTimezoneOffset() * 60000;
          const istOffset = 5.5 * 60 * 60000; // IST is UTC +5:30
          const istTime = new Date(utcOffset + istOffset);
          return istTime.toLocaleString();
        };

        const logMessage = `[${getISTTime()}] ${message}`;
        switch (level) {
          case LogLevel.Error:
            console.error(logMessage);
            return;
          case LogLevel.Info:
            console.info(logMessage);
            return;
          case LogLevel.Verbose:
            console.debug(logMessage);
            return;
          case LogLevel.Warning:
            console.warn(logMessage);
            return;
          default:
            return;
        }
      },
    },
  },
};

export const protectedResources: ProtectedResources = {
  chatAPI: {
    endpoint: 'http://localhost:8080/api/chat',
    scopes: {
      read: ['fccca4d3-5c29-410e-8fd7-409d414c9bb0/.default'],
      write: ['fccca4d3-5c29-410e-8fd7-409d414c9bb0/.default'],
    },
  },
};

export const loginRequest: LoginRequest = {
  scopes: [],
};

// Default export of a component
const AuthConfigComponent = () => {
  return null;
};

export default AuthConfigComponent;
