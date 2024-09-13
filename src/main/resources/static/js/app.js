document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('nav ul li a').forEach(link => {
        link.addEventListener('click', (event) => {
            event.preventDefault();
            showContent(link.getAttribute('href').substring(1));
        });
    });

    showContent('dentists');
    fetchData('dentists', 'dentistsTable');
    fetchData('patients', 'patientsTable');
    fetchData('appointments', 'appointmentsTable');
});

function showContent(id) {
    document.querySelectorAll('.content').forEach(content => content.classList.remove('active'));
    document.getElementById(id).classList.add('active');
}

function fetchData(entity, tableId) {
    fetch(`http://localhost:8080/${entity}`)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector(`#${tableId} tbody`);
            tableBody.innerHTML = '';
            data.forEach(item => {
                const row = document.createElement('tr');
                Object.keys(item).forEach(key => {
                    if (key !== 'appointments') {
                        const cell = document.createElement('td');
                        if (key === 'date') {
                            cell.textContent = new Date(item[key]).toLocaleString();
                        } else {
                            cell.textContent = item[key];
                        }
                        row.appendChild(cell);
                    }
                });
                const actionsCell = document.createElement('td');
                actionsCell.innerHTML = `
                    <button onclick='showForm("${entity}", "update", ${JSON.stringify(item)})'>Editar</button>
                    <button onclick="deleteItem('${entity}', ${item.id})">Eliminar</button>
                `;
                row.appendChild(actionsCell);
                tableBody.appendChild(row);
            });
        });
}

function showForm(entity, action, item = null) {
    const formContainer = document.getElementById('form');
    let formHtml = `<h2>${action === 'create' ? `Agregar ${capitalize(entity)}` : `Editar ${capitalize(entity)}`}</h2>
        <form id="${entity}Form">

                ${generateFormFields(entity,action, item)}
                <input type="hidden" name="id" value="${item?.id || ''}" />

            <button type="submit">${action === 'create' ? 'Agregar' : 'Actualizar'}</button>
        </form>`;

    formContainer.innerHTML = formHtml;
    formContainer.style.display = 'block';

    document.getElementById(`${entity}Form`).addEventListener('submit', function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        const data = Object.fromEntries(formData.entries());
        const method = action === 'create' ? 'POST' : 'PUT';
        const url = action === 'create' ? `http://localhost:8080/${entity}` : `http://localhost:8080/${entity}/${data.id}`;

        const { street, number, location, province, ...rest } = data;
        const dataRefactored = {
            ...rest,
            "address": {
                street, number, location, province
            }
        };

        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataRefactored)
        })
            .then(response => response.json())
            .then(() => {
                fetchData(entity, `${entity}Table`);
                formContainer.style.display = 'none';
            });
    });
}

function generateFormFields(entity,action, item) {
    if (entity === 'appointments') {
        if (action === 'create') {
            return `
            <label for="date">Fecha:</label>
            <input type="datetime-local" id="date" name="date" required />
            <label for="patientId">Paciente ID:</label>
            <input type="number" id="patientId" name="patientId" required />
            <label for="dentistId">Odontólogo ID:</label>
            <input type="number" id="dentistId" name="dentistId" required />
        `;
        } else if (action === 'update') {
            return `
            <label for="date">Fecha:</label>
            <input type="datetime-local" id="date" name="date" value="${new Date(item?.date).toISOString().substring(0, 16)}" required />
            <label for="patientId">Paciente ID:</label>
            <input type="number" id="patientId" name="patientId" value="${item?.patientId}" required />
            <label for="dentistId">Odontólogo ID:</label>
            <input type="number" id="dentistId" name="dentistId" value="${item?.dentistId}" required />
        `;
        }
    } else if (entity === 'patients') {
        if (action === 'create') {
            return `
                <label for="name">Nombre:</label>
                <input type="text" id="name" name="name" required />
                <label for="lastName">Apellido:</label>
                <input type="text" id="lastName" name="lastName" required />
                <label for="dni">DNI:</label>
                <input type="text" id="dni" name="dni" required />
                <label for="dischargeDate">Fecha de Alta:</label>
                <input type="date" id="dischargeDate" name="dischargeDate" required />
                <label for="street">Calle:</label>
                <input type="text" id="street" name="street" />
                <label for="number">Número:</label>
                <input type="number" id="number" name="number" />
                <label for="location">Localidad:</label>
                <input type="text" id="location" name="location" />
                <label for="province">Provincia:</label>
                <input type="text" id="province" name="province" />
            `;
        } else if (action === 'update') {
            return `
                <label for="name">Nombre:</label>
                <input type="text" id="name" name="name" value="${item?.name}" required />
                <label for="lastName">Apellido:</label>
                <input type="text" id="lastName" name="lastName" value="${item?.lastName}" required />
                <label for="dni">DNI:</label>
                <input type="text" id="dni" name="dni" value="${item?.dni}" required />
                <label for="dischargeDate">Fecha de Alta:</label>
                <input type="date" id="dischargeDate" name="dischargeDate" value="${item?.dischargeDate}" required />
                <label for="street">Calle:</label>
                <input type="text" id="street" name="street" value="${item?.address.street}" />
                <label for="number">Número:</label>
                <input type="number" id="number" name="number" value="${item?.address.number}" />
                <label for="location">Localidad:</label>
                <input type="text" id="location" name="location" value="${item?.address.location}" />
                <label for="province">Provincia:</label>
                <input type="text" id="province" name="province" value="${item?.address.province}" />
    
            `;
        }
    } else if (entity === 'dentists') {
        if (action === 'create') {
            return `
            <label for="name">Nombre:</label>
            <input type="text" id="name" name="name" required />
            <label for="lastName">Apellido:</label>
            <input type="text" id="lastName" name="lastName" required />
            <label for="licenseMedical">Licencia Médica:</label>
            <input type="text" id="licenseMedical" name="licenseMedical" required />
        `;
        } else if (action === 'update') {
            return `
            <label for="name">Nombre:</label>
            <input type="text" id="name" name="name" value="${item?.name}" required />
            <label for="lastName">Apellido:</label>
            <input type="text" id="lastName" name="lastName" value="${item?.lastName}" required />
            <label for="licenseMedical">Licencia Médica:</label>
            <input type="text" id="licenseMedical" name="licenseMedical" value="${item?.licenseMedical}" required />
        `;
        }
    }
}

function deleteItem(entity, id) {
    fetch(`http://localhost:8080/${entity}/${id}`, { method: 'DELETE' })
        .then(() => fetchData(entity, `${entity}Table`));
}

function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}
