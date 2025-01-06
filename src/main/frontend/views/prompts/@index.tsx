import React from 'react';
import { AutoCrud } from '@vaadin/hilla-react-crud';
import { TextArea } from '@vaadin/react-components';
import PromptModel from 'Frontend/generated/org/genai/fusion/model/PromptModel';
import { PromptEndpoint } from 'Frontend/generated/endpoints';

import { useSignal } from '@vaadin/hilla-react-signals';
import type { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  menu: {
    title: 'Prompts',
    order: 3,
  },
};

export default function Prompts() {
  return (
    <div className="p-m flex flex-col h-full box-border">
      <h2>Welcome to Prompts Library</h2>
      <AutoCrud
        model={PromptModel}
        service={PromptEndpoint}
        className="flex-grow"
        formProps={{
          fieldOptions: {
            prompt: {
              renderer: ({ field }) => <TextArea {...field} label="Prompt" />,
            },
          },
        }}
      />
    </div>
  );
}
