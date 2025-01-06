import React from 'react';
import { AutoCrud } from '@vaadin/hilla-react-crud';
import LoanApplicationModel from 'Frontend/generated/org/genai/fusion/model/LoanApplicationModel';
import { LoanApplicationEndpoint } from 'Frontend/generated/endpoints';

import { useSignal } from '@vaadin/hilla-react-signals';
import type { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  menu: {
    title: 'Applications',
    order: 2,
  },
};

export default function Applications() {
  return (
    <div className="p-m flex flex-col h-full box-border">
      <h2>Welcome to Application Dashboard</h2>
      <AutoCrud
        model={LoanApplicationModel}
        service={LoanApplicationEndpoint}
        className="flex-grow"
        gridProps={{
          columnOptions: {
            kycverified: {
              header: 'IsKYCVerified?',
              renderer: ({ item }) => (
                <span style={{ color: item.kycverified ? 'green' : 'red' }}>{item.kycverified ? 'Yes' : 'No'}</span>
              ),
            },
          },
        }}
      />
    </div>
  );
}
