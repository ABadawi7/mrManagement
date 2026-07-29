// Adds one safe text cell to a table row.
function addCell(row, value) {

    // Creates a new table cell.
    const cell = document.createElement("td");

    // Inserts the value as plain text.
    // HTML code is not interpreted.
    cell.textContent = value ?? "";

    // Adds the cell to the table row.
    row.appendChild(cell);
}

// Loads all employees from the backend API.
async function loadEmployees() {

    // Finds the table body in the HTML document.
    const tableBody = document.getElementById("mitarbeiterTabelle");

    try {
        // Sends a GET request to the backend.
        const response = await fetch("/api/mitarbeiter");

        // Checks whether the request was successful.
        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }

        // Converts the JSON response into a JavaScript array.
        const employees = await response.json();

        // Removes old rows before displaying the current data.
        tableBody.textContent = "";

        // Creates one table row for each employee.
        employees.forEach(employee => {

            // Creates a new table row.
            const row = document.createElement("tr");

            // Adds all employee values safely as text.
            addCell(row, employee.id);
            addCell(row, employee.mitarbeiterNr);
            addCell(row, employee.vorname);
            addCell(row, employee.nachname);
            addCell(row, employee.filialeCode);
            addCell(row, employee.rolle);
            addCell(row, employee.aktiv ? "Ja" : "Nein");
            addCell(row, employee.eintrittsdatum);

            // Adds the completed row to the table.
            tableBody.appendChild(row);
        });

    } catch (error) {
        // Removes old content when loading fails.
        tableBody.textContent = "";

        // Creates an error row.
        const errorRow = document.createElement("tr");

        // Creates an error cell.
        const errorCell = document.createElement("td");

        // The error cell covers all eight columns.
        errorCell.colSpan = 8;

        // Displays a safe error message.
        errorCell.textContent = "Mitarbeiter konnten nicht geladen werden.";

        // Adds the error cell to the row.
        errorRow.appendChild(errorCell);

        // Adds the error row to the table.
        tableBody.appendChild(errorRow);

        // Writes the technical error into the browser console.
        console.error("Failed to load employees:", error);
    }
}
// Sends the employee form data to the backend.
async function saveEmployee(event){
    // Prevents the browser form reloading the page.
    event.preventDefault();

    // Reads the values from the form.
    const employee = {
        vorname: document.getElementById("vorname").value.trim(),
        nachname: document.getElementById("nachname").value.trim(),
        filialeCode: document.getElementById("filialCode").value,
        rolle: document.getElementById("rolle").value,
        aktiv: document.getElementById("aktiv").checked,
        eintrittsdatum: document.getElementById("eintrittsdatum").value || null
    };

    // Sends the employee data to the backend.

    const response = await fetch("api/mitarbeiter",{
       method: "POST",
       headers: {
           "Content-Type":"application/json"
       } ,
        body: JSON.stringify(employee)
    });

    // Reads the response message from the backend.
    const message = await response.text();

    // Displays the message on the page.
    document.getElementById("meldung").textContent = message;

    if(response.ok){

        // Clears the form after successful saving.
        document.getElementById("mitarbeiterFormular").reset();

        // Activates the checkbox again after resetting.
        document.getElementById("aktiv").checked = true;

        // Reloads the employee table.
        await loadEmployees();
    }
}

// Loads the employees after the HTML page is fully loaded.
document.addEventListener("DOMContentLoaded", loadEmployees);

// Connects the form with the save function.
document.getElementById("mitarbeiterFormular").addEventListener("submit",saveEmployee);