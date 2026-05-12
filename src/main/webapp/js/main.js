document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".alert").forEach(function (alert) {
        if (alert.querySelector(".alert-close")) {
            return;
        }
        const closeButton = document.createElement("button");
        closeButton.className = "alert-close";
        closeButton.type = "button";
        closeButton.setAttribute("aria-label", "Dismiss message");
        closeButton.innerHTML = '<span class="material-symbols-outlined">close</span>';
        closeButton.addEventListener("click", function () {
            alert.remove();
        });
        alert.appendChild(closeButton);
    });

    initConfirmModal();
    initAppointmentCancelWarning();
    initNoFeeAppointmentCancelWarning();
    initDayPickers();

    initAdminModal("doctorModal", "openDoctorModal");
    initAdminModal("patientModal", "openPatientModal");
    initAdminModal("departmentModal", "openDepartmentModal");

    const deptSelect = document.getElementById("deptId");
    const doctorSelect = document.getElementById("doctorId");
    const doctorAvailabilityNote = document.getElementById("doctorAvailabilityNote");
    if (deptSelect && doctorSelect) {
        updateDoctorAvailabilityNote(doctorSelect, doctorAvailabilityNote);
        doctorSelect.addEventListener("change", function () {
            updateDoctorAvailabilityNote(doctorSelect, doctorAvailabilityNote);
        });

        deptSelect.addEventListener("change", function () {
            if (!deptSelect.value) {
                doctorSelect.innerHTML = '<option value="">-- Select Doctor --</option>';
                doctorSelect.disabled = true;
                updateDoctorAvailabilityNote(doctorSelect, doctorAvailabilityNote);
                return;
            }
            doctorSelect.disabled = true;
            doctorSelect.innerHTML = '<option value="">Loading doctors...</option>';
            fetch(window.contextPath + "/api/doctors?deptId=" + encodeURIComponent(deptSelect.value))
                .then(function (response) { return response.json(); })
                .then(function (doctors) {
                    doctorSelect.innerHTML = '<option value="">-- Select Doctor --</option>';
                    doctors.forEach(function (doctor) {
                        const option = document.createElement("option");
                        option.value = doctor.doctorId;
                        option.dataset.availableDays = doctor.availableDays || "";
                        option.textContent = "Dr. " + doctor.fullName + " - " + doctor.specialization
                            + (doctor.availableDays ? " (" + doctor.availableDays + ")" : "");
                        doctorSelect.appendChild(option);
                    });
                    doctorSelect.disabled = false;
                    updateDoctorAvailabilityNote(doctorSelect, doctorAvailabilityNote);
                })
                .catch(function () {
                    doctorSelect.innerHTML = '<option value="">Unable to load doctors</option>';
                    doctorSelect.disabled = true;
                    updateDoctorAvailabilityNote(doctorSelect, doctorAvailabilityNote);
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

    const medicalNewsGrid = document.getElementById("medicalNewsGrid");
    if (medicalNewsGrid) {
        fetch(window.contextPath + "/api/medical-news")
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("News request failed");
                }
                return response.json();
            })
            .then(function (data) {
                const items = normalizeMedicalNewsItems(data).slice(0, 6);
                if (items.length === 0) {
                    renderMedicalNewsState(medicalNewsGrid, "No health topics are available right now.", apiMessage(data) || "Try again in a few minutes.");
                    return;
                }

                medicalNewsGrid.innerHTML = "";
                items.forEach(function (item) {
                    const card = document.createElement("article");
                    card.className = "news-card";

                    const meta = document.createElement("span");
                    meta.className = "news-meta";
                    const sourceLabel = sourceName(item.source) || item.sourceName || item.author || "MyHealthfinder";
                    const dateLabel = formatNewsDate(item.publishedAt || item.published_at || item.date);
                    meta.textContent = sourceLabel + (dateLabel ? " - " + dateLabel : "");

                    const title = document.createElement("strong");
                    title.textContent = item.Title || item.title || "Health topic";

                    const summary = document.createElement("p");
                    summary.textContent = item.description || item.summary || item.content || topicSummary(item);

                    const link = document.createElement("a");
                    link.className = "news-link";
                    link.href = topicLink(item);
                    link.target = "_blank";
                    link.rel = "noopener";
                    link.innerHTML = 'View topic <span class="material-symbols-outlined">open_in_new</span>';

                    card.appendChild(meta);
                    card.appendChild(title);
                    card.appendChild(summary);
                    card.appendChild(link);
                    medicalNewsGrid.appendChild(card);
                });
            })
            .catch(function () {
                renderMedicalNewsState(medicalNewsGrid, "Health topics could not be loaded.", "Please check the server internet connection and refresh.");
            });
    }
});

function normalizeMedicalNewsItems(data) {
    if (Array.isArray(data)) {
        return data;
    }
    if (!data || typeof data !== "object") {
        return [];
    }
    return firstArray(
        data.articles,
        data.items,
        data.news,
        data.data,
        data.results,
        data.Result && data.Result.Items && data.Result.Items.Item
    );
}

function initAdminModal(modalId, openButtonId) {
    const modal = document.getElementById(modalId);
    const openButton = document.getElementById(openButtonId);
    if (!modal || !openButton) {
        return;
    }

    const closeModal = function () {
        if (modal.dataset.resetUrl) {
            window.location.href = modal.dataset.resetUrl;
            return;
        }
        modal.classList.remove("is-open");
        modal.setAttribute("aria-hidden", "true");
    };

    openButton.addEventListener("click", function () {
        modal.classList.add("is-open");
        modal.setAttribute("aria-hidden", "false");
        const firstField = modal.querySelector("input:not([type='hidden']), select, textarea");
        if (firstField) {
            firstField.focus();
        }
    });

    modal.querySelectorAll("[data-modal-close]").forEach(function (button) {
        button.addEventListener("click", closeModal);
    });

    modal.addEventListener("click", function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && modal.classList.contains("is-open")) {
            closeModal();
        }
    });
}

function updateDoctorAvailabilityNote(doctorSelect, note) {
    if (!doctorSelect || !note) {
        return;
    }
    const option = doctorSelect.options[doctorSelect.selectedIndex];
    const availableDays = option ? option.dataset.availableDays : "";
    if (doctorSelect.disabled) {
        note.textContent = "Select a department first.";
        return;
    }
    note.textContent = availableDays ? "Available days: " + availableDays : "Select a doctor to see available days.";
}

function initDayPickers() {
    document.querySelectorAll(".day-picker").forEach(function (picker) {
        const target = document.getElementById(picker.dataset.target);
        const summary = document.getElementById("availableDaysSummary");
        const updateValue = function () {
            const selected = Array.from(picker.querySelectorAll("input:checked")).map(function (input) {
                return input.value;
            });
            if (target) {
                target.value = selected.join(", ");
            }
            if (summary) {
                summary.textContent = selected.length ? "Selected: " + selected.join(", ") : "Select available days.";
            }
        };

        picker.querySelectorAll("input").forEach(function (input) {
            input.addEventListener("change", updateValue);
        });
        updateValue();
    });
}

function initConfirmModal() {
    const confirmModal = document.getElementById("confirmModal");
    const title = document.getElementById("confirmModalTitle");
    const message = document.getElementById("confirmModalMessage");
    const acceptButton = document.getElementById("acceptConfirm");
    const cancelButtons = [
        document.getElementById("cancelConfirm"),
        document.getElementById("cancelConfirmIcon")
    ].filter(Boolean);
    let pendingForm = null;

    if (!confirmModal || !title || !message || !acceptButton) {
        return;
    }

    const closeConfirm = function () {
        pendingForm = null;
        confirmModal.classList.remove("is-open");
        confirmModal.setAttribute("aria-hidden", "true");
    };

    document.querySelectorAll(".btn-delete").forEach(function (button) {
        button.addEventListener("click", function (event) {
            const form = button.closest("form");
            if (!form) {
                return;
            }
            event.preventDefault();
            pendingForm = form;

            const action = form.getAttribute("action") || "";
            const entityName = deleteEntityName(action);
            const rowLabel = deleteRowLabel(button);
            title.textContent = "Delete " + entityName + "?";
            message.textContent = rowLabel
                ? "You are about to delete " + rowLabel + ". This action cannot be undone."
                : "This action cannot be undone.";
            acceptButton.textContent = "Delete " + entityName;

            confirmModal.classList.add("is-open");
            confirmModal.setAttribute("aria-hidden", "false");
            acceptButton.focus();
        });
    });

    acceptButton.addEventListener("click", function () {
        if (pendingForm) {
            pendingForm.submit();
        }
    });

    cancelButtons.forEach(function (button) {
        button.addEventListener("click", closeConfirm);
    });

    confirmModal.addEventListener("click", function (event) {
        if (event.target === confirmModal) {
            closeConfirm();
        }
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && confirmModal.classList.contains("is-open")) {
            closeConfirm();
        }
    });
}

function initAppointmentCancelWarning() {
    const confirmModal = document.getElementById("confirmModal");
    const title = document.getElementById("confirmModalTitle");
    const message = document.getElementById("confirmModalMessage");
    const acceptButton = document.getElementById("acceptConfirm");
    const cancelButtons = [
        document.getElementById("cancelConfirm"),
        document.getElementById("cancelConfirmIcon")
    ].filter(Boolean);
    let pendingForm = null;

    if (!confirmModal || !title || !message || !acceptButton) {
        return;
    }

    const closeConfirm = function () {
        pendingForm = null;
        confirmModal.classList.remove("is-open");
        confirmModal.setAttribute("aria-hidden", "true");
    };

    document.querySelectorAll(".btn-cancel-appointment").forEach(function (button) {
        button.addEventListener("click", function (event) {
            const form = button.closest("form");
            if (!form) {
                return;
            }
            event.preventDefault();
            pendingForm = form;

            title.textContent = "Cancel appointment?";
            message.textContent = "If you cancel this appointment, you will still need to pay 50% of the consultation fee.";
            acceptButton.textContent = "Cancel Appointment";

            confirmModal.classList.add("is-open");
            confirmModal.setAttribute("aria-hidden", "false");
            acceptButton.focus();
        });
    });

    acceptButton.addEventListener("click", function () {
        if (pendingForm) {
            pendingForm.submit();
        }
    });

    cancelButtons.forEach(function (button) {
        button.addEventListener("click", closeConfirm);
    });

    confirmModal.addEventListener("click", function (event) {
        if (event.target === confirmModal) {
            closeConfirm();
        }
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && confirmModal.classList.contains("is-open")) {
            closeConfirm();
        }
    });
}

function initNoFeeAppointmentCancelWarning() {
    initFormConfirm(".btn-cancel-no-fee", "Cancel appointment?", "This appointment will be cancelled and the patient will not be charged.", "Cancel Appointment", true);
}

function initFormConfirm(buttonSelector, dialogTitle, dialogMessage, acceptLabel, requiresReason) {
    const confirmModal = document.getElementById("confirmModal");
    const title = document.getElementById("confirmModalTitle");
    const message = document.getElementById("confirmModalMessage");
    const acceptButton = document.getElementById("acceptConfirm");
    const reasonField = document.getElementById("confirmReasonField");
    const reasonInput = document.getElementById("confirmReasonInput");
    const cancelButtons = [
        document.getElementById("cancelConfirm"),
        document.getElementById("cancelConfirmIcon")
    ].filter(Boolean);
    let pendingForm = null;

    if (!confirmModal || !title || !message || !acceptButton) {
        return;
    }

    const closeConfirm = function () {
        pendingForm = null;
        if (reasonField) {
            reasonField.hidden = true;
        }
        if (reasonInput) {
            reasonInput.value = "";
        }
        confirmModal.classList.remove("is-open");
        confirmModal.setAttribute("aria-hidden", "true");
    };

    document.querySelectorAll(buttonSelector).forEach(function (button) {
        button.addEventListener("click", function (event) {
            const form = button.closest("form");
            if (!form) {
                return;
            }
            event.preventDefault();
            pendingForm = form;
            title.textContent = dialogTitle;
            message.textContent = dialogMessage;
            acceptButton.textContent = acceptLabel;
            if (reasonField && reasonInput) {
                reasonField.hidden = !requiresReason;
                reasonInput.value = "";
            }
            confirmModal.classList.add("is-open");
            confirmModal.setAttribute("aria-hidden", "false");
            if (requiresReason && reasonInput) {
                reasonInput.focus();
            } else {
                acceptButton.focus();
            }
        });
    });

    acceptButton.addEventListener("click", function () {
        if (pendingForm) {
            if (requiresReason && reasonInput) {
                const reason = reasonInput.value.trim();
                if (!reason) {
                    reasonInput.focus();
                    return;
                }
                let hiddenReason = pendingForm.querySelector("input[name='cancelReason']");
                if (!hiddenReason) {
                    hiddenReason = document.createElement("input");
                    hiddenReason.type = "hidden";
                    hiddenReason.name = "cancelReason";
                    pendingForm.appendChild(hiddenReason);
                }
                hiddenReason.value = reason;
            }
            pendingForm.submit();
        }
    });

    cancelButtons.forEach(function (button) {
        button.addEventListener("click", closeConfirm);
    });

    confirmModal.addEventListener("click", function (event) {
        if (event.target === confirmModal) {
            closeConfirm();
        }
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && confirmModal.classList.contains("is-open")) {
            closeConfirm();
        }
    });
}

function deleteEntityName(action) {
    if (action.indexOf("delete-record") !== -1) {
        return "Medical Record";
    }
    if (action.indexOf("delete-department") !== -1) {
        return "Department";
    }
    if (action.indexOf("delete-patient") !== -1) {
        return "Patient";
    }
    if (action.indexOf("delete-doctor") !== -1) {
        return "Doctor";
    }
    return "Item";
}

function deleteRowLabel(button) {
    const row = button.closest("tr");
    if (!row) {
        return "";
    }
    const firstCell = row.querySelector("td");
    return firstCell ? firstCell.textContent.trim() : "";
}

function firstArray() {
    for (let i = 0; i < arguments.length; i++) {
        if (Array.isArray(arguments[i])) {
            return arguments[i];
        }
    }
    return [];
}

function sourceName(source) {
    if (!source) {
        return "";
    }
    if (typeof source === "string") {
        return source;
    }
    return source.name || "";
}

function topicSummary(item) {
    if (item.Type || topicId(item)) {
        return "Official preventive health guidance from MyHealthfinder.";
    }
    return "Open the topic to read the full guidance.";
}

function topicLink(item) {
    const id = topicId(item);
    if (id) {
        return window.contextPath + "/api/medical-news?topicId=" + encodeURIComponent(id) + "&redirect=true";
    }
    return item.url || item.link || "https://odphp.health.gov/myhealthfinder/topics";
}

function topicId(item) {
    if (!item || typeof item !== "object") {
        return "";
    }
    return item.Id || item.ID || item.id || item.TopicId || item.topicId || item.TranslationId || item.translationId || "";
}

function apiMessage(data) {
    if (!data || typeof data !== "object") {
        return "";
    }
    return data.message || data.Message || data.Error || (data.Result && data.Result.Error);
}

function renderMedicalNewsState(container, title, message) {
    container.innerHTML = "";
    const card = document.createElement("article");
    card.className = "news-card news-card-loading";
    card.innerHTML = '<span class="feature-icon"><span class="material-symbols-outlined">info</span></span>';

    const strong = document.createElement("strong");
    strong.textContent = title;
    const copy = document.createElement("p");
    copy.textContent = message;

    card.appendChild(strong);
    card.appendChild(copy);
    container.appendChild(card);
}

function formatNewsDate(value) {
    if (!value) {
        return "";
    }
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
        return value;
    }
    return date.toLocaleDateString(undefined, {
        month: "short",
        day: "numeric",
        year: "numeric"
    });
}
