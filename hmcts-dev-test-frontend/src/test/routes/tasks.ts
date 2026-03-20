import { app } from '../../main/app';
import { expect } from 'chai';
import nock from 'nock';
import request from 'supertest';

/* eslint-disable jest/expect-expect */

describe('Tasks routes', () => {
    const backendUrl = 'http://localhost:4000';

    const sampleTasks = [
        {
            id: 1,
            title: 'This is a test',
            description: 'Here is a description',
            status: 'TODO',
            dueDateTime: '2026-03-20T10:00:00',
        },
    ];

    // Mock the backend GET /tasks response so the page can render predictable data
    const mockGetTasks = () => nock(backendUrl).get('/tasks').reply(200, sampleTasks);

    afterEach(() => {
        // Reset nock between tests so mocks don't leak across cases
        nock.cleanAll();
    });

    test('GET /tasks should render tasks page with data', async () => {
        const scope = mockGetTasks();

        const res = await request(app).get('/tasks').expect(200);

        expect(res.text).to.contain('This is a test');
        expect(res.text).to.contain('Here is a description');

        expect(scope.isDone()).to.equal(true);
    });

    test('POST /tasks with missing fields should show validation errors', async () => {
        const res = await request(app)
            .post('/tasks')
            .type('form')
            .send({ title: '', status: '', dueDateTime: '' })
            .expect(400);

        expect(res.text).to.contain('There is a problem');
        expect(res.text).to.contain('Title is required');
    });

    test('POST /tasks should call backend and redirect', async () => {
        const scope = nock(backendUrl)
            .post('/tasks', (body: any) => {
                expect(body.title).to.equal('Test task');
                expect(body.status).to.equal('TODO');
                return true;
            })
            .reply(200, { id: 1 });

        await request(app)
            .post('/tasks')
            .type('form')
            .send({
                title: 'Test task',
                status: 'TODO',
                dueDateTime: '2026-03-20T10:00',
            })
            .expect(302) // POST then redirect back to the list page
            .expect('Location', '/tasks');

        expect(scope.isDone()).to.equal(true);
    });

    test('POST /tasks/:id/delete should call backend and redirect', async () => {
        const scope = nock(backendUrl)
            .delete('/tasks/1')
            .reply(204); // 204 = delete succeeded (no response body)

        await request(app)
            .post('/tasks/1/delete')
            .expect(302)
            .expect('Location', '/tasks');

        expect(scope.isDone()).to.equal(true);
    });
});