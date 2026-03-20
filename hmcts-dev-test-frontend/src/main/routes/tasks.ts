import { Application } from 'express';
import axios from 'axios';

const backendUrl = process.env.BACKEND_URL ?? 'http://localhost:4000';

export default function (app: Application): void {
    app.get('/tasks', async (req, res) => {
        try {
            const response = await axios.get(`${backendUrl}/tasks`);
            res.render('tasks', { tasks: response.data, form: {}, errors: [] });
        } catch (error) {
            console.error('Error making request:', error);
            res.render('tasks', { tasks: [], form: {}, errors: ['Unable to load tasks. Is the backend running?'] });
        }
    });

    app.post('/tasks', async (req, res) => {
        const { title, description, status, dueDateTime } = req.body;

        const errors: string[] = [];
        if (!title || String(title).trim() === '') errors.push('Title is required');
        if (!status) errors.push('Status is required');
        if (!dueDateTime) errors.push('Due date/time is required');

        if (errors.length > 0) {
            try {
                const response = await axios.get(`${backendUrl}/tasks`);
                return res.status(400).render('tasks', {
                    tasks: response.data,
                    form: { title, description, status, dueDateTime },
                    errors,
                });
            } catch (error) {
                console.error('Error making request:', error);
                return res.status(400).render('tasks', {
                    tasks: [],
                    form: { title, description, status, dueDateTime },
                    errors,
                });
            }
        }

        try {
            await axios.post(`${backendUrl}/tasks`, {
                title,
                description: description || undefined,
                status,
                dueDateTime,
            });

            return res.redirect('/tasks');
        } catch (error: any) {
            console.error('Error making request:', error);

            const message =
                error?.response?.data
                    ? `Failed to create task: ${JSON.stringify(error.response.data)}`
                    : 'Failed to create task';

            try {
                const response = await axios.get(`${backendUrl}/tasks`);
                return res.status(400).render('tasks', {
                    tasks: response.data,
                    form: { title, description, status, dueDateTime },
                    errors: [message],
                });
            } catch {
                return res.status(400).render('tasks', {
                    tasks: [],
                    form: { title, description, status, dueDateTime },
                    errors: [message],
                });
            }
        }
    });

    app.post('/tasks/:id/status', async (req, res) => {
        const id = Number(req.params.id);
        const { status } = req.body;

        try {
            await axios.patch(`${backendUrl}/tasks/${id}/status`, { status });
            return res.redirect('/tasks');
        } catch (error: any) {
            console.error('Error making request:', error);

            const message =
                error?.response?.data
                    ? `Failed to update status: ${JSON.stringify(error.response.data)}`
                    : 'Failed to update status';

            try {
                const response = await axios.get(`${backendUrl}/tasks`);
                return res.status(400).render('tasks', { tasks: response.data, form: {}, errors: [message] });
            } catch {
                return res.status(400).render('tasks', { tasks: [], form: {}, errors: [message] });
            }
        }
    });

    app.post('/tasks/:id/delete', async (req, res) => {
        const id = Number(req.params.id);

        try {
            await axios.delete(`${backendUrl}/tasks/${id}`);
            return res.redirect('/tasks');
        } catch (error: any) {
            console.error('Error making request:', error);

            const message =
                error?.response?.data
                    ? `Failed to delete task: ${JSON.stringify(error.response.data)}`
                    : 'Failed to delete task';

            try {
                const response = await axios.get(`${backendUrl}/tasks`);
                return res.status(400).render('tasks', { tasks: response.data, form: {}, errors: [message] });
            } catch {
                return res.status(400).render('tasks', { tasks: [], form: {}, errors: [message] });
            }
        }
    });
}