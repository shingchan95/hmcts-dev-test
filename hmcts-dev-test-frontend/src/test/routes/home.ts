import { app } from '../../main/app';

import { expect } from 'chai';
import request from 'supertest';

// TODO: replace this sample test with proper route tests for your application
/* eslint-disable jest/expect-expect */
describe('Home page', () => {
  describe('on GET', () => {
    test('should return the home page', async () => {
      await request(app)
        .get('/')
        .expect(200)
        .expect((res: any) => {
          expect(res.text).to.contain('Welcome to HMCTS tasks tracker!');
          expect(res.text).to.contain('href="/tasks"');
        });
    });
  });
});
