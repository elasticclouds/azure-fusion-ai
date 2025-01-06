import { useState } from 'react';
import { ChatEndpoint } from 'Frontend/generated/endpoints.js';
import { useSignal } from '@vaadin/hilla-react-signals';
import type { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { MessageList, MessageListItem, MessageInput } from '@vaadin/react-components';
import { nanoid } from 'nanoid';
import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';
import { loginRequest, protectedResources } from './components/authConfig';
import useFetchWithMsal from './components/useFetchWithMsal';
import { from } from 'rxjs';
import { ProgressBar } from 'react-loader-spinner';

export const config: ViewConfig = {
  menu: {
    title: 'FusionGenie',
    order: 1,
  },
};

export default function MainView() {
  const name = useSignal('');
  const [messages, setMessages] = useState<MessageListItem[]>([]);
  const [message, setMessage] = useState('Hey Genie, ');
  const [chatId, setChatId] = useState(nanoid());
  const [isLoading, setIsLoading] = useState(false);

  // Function to show Loading Spinner
  const showLoading = () => {
    console.log('isLoading:', isLoading);
    return isLoading ? (
      <div className="flex justify-center items-center h-full">
        <ProgressBar
          visible={isLoading}
          height="200"
          width="200"
          ariaLabel="progress-bar-loading"
          wrapperStyle={{}}
          wrapperClass=""
        />
      </div>
    ) : (
      <b></b>
    );
  };

  const { error, execute } = useFetchWithMsal({
    scopes: protectedResources.chatAPI.scopes.read,
  });

  const { instance } = useMsal();
  const activeAccount = instance.getActiveAccount();

  async function sendMessage(message: string) {
    setIsLoading(true);
    setMessage(message);
    setMessages((messages) => [
      ...messages,
      {
        text: message,
        userName: 'You',
        userColorIndex: 1,
        className: 'current-user',
        userImg: 'https://cdn-icons-png.flaticon.com/256/4042/4042356.png',
      },
    ]);

    let response = '';

    let first = true;
    ChatEndpoint.chat(chatId, message)
      .onNext((token) => {
        if (first && token) {
          console.log(token);

          setMessages((messages) => [
            ...messages,
            {
              text: token,
              userName: 'Agent',
              userColorIndex: 2,
              userImg: 'https://cdn-icons-png.flaticon.com/512/13298/13298228.png',
            },
          ]);

          first = false;
        } else {
          appendToLatestMessage(token);
        }
        setIsLoading(false);
      })
      .onError(() => {
        setIsLoading(false);
        setMessages((messages) => [
          ...messages,
          {
            text: 'Error fetching data from API',
            userName: 'Agent',
            userColorIndex: 2,
            userImg: 'https://cdn-icons-png.flaticon.com/512/13298/13298228.png',
          },
        ]);
        console.error('Error fetching data from API');
      })
      .onComplete(() => {
        setIsLoading(false);
      });
  }

  function appendToLatestMessage(chunk: string) {
    setMessages((messages) => {
      const latestMessage = messages[messages.length - 1];
      latestMessage.text += chunk;
      return [...messages.slice(0, -1), latestMessage];
    });
  }

  return (
    <>
      <AuthenticatedTemplate>
        {activeAccount ? (
          <>
            <div className="p-m flex flex-col h-full box-border" style={{ overflowX: 'hidden', position: 'relative' }}>
              {showLoading()}
              {messages.length === 0 ? (
                <div className="welcome-message flex-grow">
                  <h2>Welcome, {activeAccount.name}!!</h2>
                  <p>Start a conversation by typing a message below.</p>
                </div>
              ) : (
                <MessageList items={messages} className="flex-grow" />
              )}
              <MessageInput onSubmit={(e) => sendMessage(e.detail.value)} value={message} />
            </div>
            <br />
          </>
        ) : null}
      </AuthenticatedTemplate>
      <UnauthenticatedTemplate>
        {window.location.href === 'http://azure-fusion-devpost.eastus.cloudapp.azure.com/' ? (
          <div className="p-m flex flex-col h-full box-border" style={{ overflowX: 'hidden', position: 'relative' }}>
            <div className="welcome-message flex-grow">
              <h2>Welcome!!</h2>
              <p>Start a conversation by typing a message below.</p>
            </div>
            <MessageList items={messages} className="flex-grow" />
            <MessageInput onSubmit={(e) => sendMessage(e.detail.value)} value={message} />
          </div>
        ) : (
          <span
            className="text-m m-0"
            style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', width: '100%' }}>
            You are not signed in. Please sign in to continue!!
          </span>
        )}
      </UnauthenticatedTemplate>
    </>
  );
}
