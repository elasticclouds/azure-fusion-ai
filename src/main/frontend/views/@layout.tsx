import { AppLayout, DrawerToggle, ProgressBar, SideNav, SideNavItem } from '@vaadin/react-components';
import { createMenuItems, useViewConfig } from '@vaadin/hilla-file-router/runtime.js';
import { Suspense, useEffect, useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { Signal, signal, effect } from '@vaadin/hilla-react-signals';

import { PublicClientApplication, EventType, AccountInfo, AuthenticationResult } from '@azure/msal-browser';
import { msalConfig, loginRequest } from './components/authConfig';
import { MsalProvider, AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';

import { Button, Notification, TextField } from '@vaadin/react-components';
import { MessageList, MessageListItem, MessageInput } from '@vaadin/react-components';

/**
 * MSAL should be instantiated outside of the component tree to prevent it from being re-instantiated on re-renders.
 * For more, visit: https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-react/docs/getting-started.md
 */
const msalInstance = new PublicClientApplication(msalConfig);

// Default to using the first account if no account is active on page load
console.log('msalInstance.getAllAccounts().length :', msalInstance.getAllAccounts().length);
if (!msalInstance.getActiveAccount() && msalInstance.getAllAccounts().length > 0) {
  // Account selection logic is app dependent. Adjust as needed for different use cases.
  msalInstance.setActiveAccount(msalInstance.getAllAccounts()[0]);
  console.log('msalInstance.getAllAccounts()[0] : ', msalInstance.getAllAccounts()[0]);
}

// Optional - This will update account state if a user signs in from another tab or window
msalInstance.enableAccountStorageEvents();

// Listen for sign-in event and set active account
msalInstance.addEventCallback((event) => {
  const authenticationResult = event.payload as AuthenticationResult;
  const account = authenticationResult?.account;
  if (event.eventType === EventType.LOGIN_SUCCESS && account) {
    msalInstance.setActiveAccount(account);
  }
});

const vaadin = window.Vaadin as {
  documentTitleSignal: Signal<string>;
};
vaadin.documentTitleSignal = signal('AzureFusionAI');
effect(() => {
  document.title = vaadin.documentTitleSignal.value;
});

export default function MainLayout() {
  const currentTitle = useViewConfig()?.title ?? '';
  const navigate = useNavigate();
  const location = useLocation();

  const handleLoginRedirect = () => {
    msalInstance
      .loginRedirect({
        ...loginRequest,
        prompt: 'create',
      })
      .catch((error: any) => console.log(error));
  };

  const handleLogoutRedirect = () => {
    msalInstance.logoutRedirect({
      postLogoutRedirectUri: window.location.origin,
      onRedirectNavigate: (url) => {
        // Prevent automatic redirection to login
        if (url.includes('login')) {
          return false;
        }
        return true;
      },
    });
  };

  return (
    <MsalProvider instance={msalInstance}>
      <AppLayout style={{ margin: '20px' }} primarySection="drawer">
        <div slot="drawer" className="flex flex-col justify-between h-full p-m">
          <header className="flex flex-col gap-m">
            <h1 className="text-l m-0">{vaadin.documentTitleSignal}</h1>
            <SideNav onNavigate={({ path }) => navigate(path!)} location={location}>
              {createMenuItems()
                .filter(({ to }) => !to.startsWith('/components'))
                .map(({ to, title }) => (
                  <SideNavItem path={to} key={to}>
                    {title}
                  </SideNavItem>
                ))}
            </SideNav>
          </header>
        </div>

        <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
        <h2 slot="navbar" className="text-l m-0">
          {vaadin.documentTitleSignal}
        </h2>
        <span
          slot="navbar"
          style={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', width: '100%' }}>
          <AuthenticatedTemplate>
            <Button onClick={handleLogoutRedirect}>Logout</Button> &nbsp;
          </AuthenticatedTemplate>
          <UnauthenticatedTemplate>
            <Button onClick={handleLoginRedirect}>Login</Button>&nbsp;
          </UnauthenticatedTemplate>
        </span>

        <Suspense fallback={<ProgressBar indeterminate className="m-0" />}>
          <section className="view h-full overflow-auto" style={{ overflowX: 'hidden' }}>
            <Outlet />
          </section>
        </Suspense>
      </AppLayout>
    </MsalProvider>
  );
}
