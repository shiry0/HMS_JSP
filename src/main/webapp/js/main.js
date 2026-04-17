document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".alert").forEach(function (alert) {
        setTimeout(function () {
            alert.remove();
        }, 4000);
    });

    document.querySelectorAll(".btn-delete").forEach(function (button) {
        button.addEventListener("click", function (event) {
            if (!confirm("Are you sure you want to delete this?")) {
                event.preventDefault();
            }
        });
    });

    const deptSelect = document.getElementById("deptId");
    const doctorSelect = document.getElementById("doctorId");
    if (deptSelect && doctorSelect) {
        deptSelect.addEventListener("change", function () {
            if (!deptSelect.value) {
                return;
            }
            fetch(window.contextPath + "/api/doctors?deptId=" + encodeURIComponent(deptSelect.value))
                .then(function (response) { return response.json(); })
                .then(function (doctors) {
                    doctorSelect.innerHTML = '<option value="">-- Select Doctor --</option>';
                    doctors.forEach(function (doctor) {
                        const option = document.createElement("option");
                        option.value = doctor.doctorId;
                        option.textContent = "Dr. " + doctor.fullName + " - " + doctor.specialization;
                        doctorSelect.appendChild(option);
                    });
                });
        });
    }

    const addRxRow = document.getElementById("addRxRow");
    if (addRxRow) {
        addRxRow.addEventListener("click", function () {
            const tbody = document.querySelector("#rxTable tbody");
            const firstRow = tbody.querySelector("tr");
            if (!firstRow) {
                return;
            }
            const row = firstRow.cloneNode(true);
            row.querySelectorAll("input").forEach(function (input) {
                input.value = "";
            });
            tbody.appendChild(row);
        });
    }
});
