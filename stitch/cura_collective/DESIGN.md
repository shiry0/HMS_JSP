# Design System Specification: The Clinical Atelier

## 1. Overview & Creative North Star
The "Creative North Star" for this design system is **"The Digital Sanctuary."** 

In the high-stakes environment of healthcare, we move beyond the cold, sterile aesthetic of traditional legacy software. Instead, we embrace a "High-End Editorial" approach to medical data. We treat a patient’s record not as a database entry, but as a prestigious document. By utilizing intentional asymmetry, expansive white space, and a sophisticated layering of soft surfaces, we create a system that feels surgically precise yet human-centric. This is "Soft Minimalism" applied to high-utility dashboards—reducing cognitive load for doctors while providing a sense of secure, premium care for patients.

---

## 2. Colors & Surface Philosophy
Our palette is rooted in tranquility and clinical precision. We utilize a Material Design-inspired token system to ensure logical depth and accessibility.

### The "No-Line" Rule
**Explicit Instruction:** Designers are prohibited from using 1px solid borders for sectioning or containment. Traditional lines create "visual noise" that fatigues the eyes during long shifts. Boundaries must be defined solely through background color shifts (e.g., a `surface-container-low` section sitting on a `surface` background) or subtle tonal transitions.

### Surface Hierarchy & Nesting
Treat the UI as a series of physical layers—like stacked sheets of frosted glass.
*   **Base:** `surface` (#F8F9FA) - The canvas.
*   **Lower Tier:** `surface-container-low` (#F3F4F5) - Sub-navigation or secondary sidebars.
*   **Primary Tier:** `surface-container-lowest` (#FFFFFF) - The main content cards where data lives.
*   **Highest Tier:** `surface-container-highest` (#E1E3E4) - High-priority popovers or active state indicators.

### The "Glass & Gradient" Rule
To elevate the experience from "standard" to "signature," use Glassmorphism for floating elements (like top navigation bars or floating action buttons).
*   **Floating Elements:** Use a semi-transparent `surface` color with a `24px` backdrop-blur.
*   **Signature Textures:** For primary CTAs and critical data visualizations, use a subtle linear gradient from `primary` (#0059BB) to `primary-container` (#0070EA) at a 135° angle. This provides a "visual soul" that flat color cannot achieve.

---

## 3. Typography: The Editorial Authority
We utilize a dual-font strategy to balance character with readability.

*   **Display & Headlines (Manrope):** Use Manrope for all `display-` and `headline-` scales. Its geometric yet friendly curves provide a modern, "Editorial" feel that differentiates the HMS from generic Bootstrap apps.
*   **Interface & Body (Inter):** Use Inter for `title-`, `body-`, and `label-` scales. Inter is optimized for small-screen readability, crucial for medical charts and dosage instructions.

**Hierarchy as Brand Identity:**
*   **High Contrast:** Use `display-md` (2.75rem) for dashboard welcomes to create a spacious, welcoming entry point.
*   **Dense Utility:** Use `label-sm` (0.6875rem) in `on-surface-variant` for metadata to ensure the UI remains powerful for admins without feeling cluttered.

---

## 4. Elevation & Depth
Depth in this system is achieved through **Tonal Layering** rather than structural lines.

### The Layering Principle
Depth is created by "stacking." A patient's vitals card (`surface-container-lowest`) should sit on a background of `surface-container-low`. The 2-tone shift creates a natural, soft lift.

### Ambient Shadows
Shadows are reserved for "Floating" states only (Modals, Dropdowns).
*   **Blur:** 32px to 48px.
*   **Opacity:** 4% to 8%.
*   **Color:** Use a tinted version of `on-surface` (e.g., `#191C1D` at 6% alpha) to mimic natural, ambient light.

### The "Ghost Border" Fallback
If a border is required for extreme accessibility cases, use a **Ghost Border**: The `outline-variant` token at 15% opacity. **Never use 100% opaque borders.**

---

## 5. Components
Our primitives emphasize the **XL Roundedness Scale** (1.5rem / 24px) for high-level containers to soften the clinical environment.

### Buttons & Chips
*   **Primary Button:** Gradient fill (`primary` to `primary-container`), `xl` corner radius, white text.
*   **Secondary/Tertiary:** Use `surface-container-high` as a base with `primary` text. No borders.
*   **Selection Chips:** Use `secondary-fixed` (#A4EEFF) for active states to provide a calming Teal accent that signifies "Action Taken."

### Input Fields & Search
*   **Styling:** Large, 16px padding, `md` (0.75rem) corners.
*   **Background:** Use `surface-container-low` with a `2px` focus transition to `primary`.
*   **Validation:** Error states use `error` (#BA1A1A) text but `error-container` (#FFDAD6) as a soft background wash—never a red border.

### Cards & Lists (The "Anti-Grid" Rule)
*   **Forbid Dividers:** Do not use horizontal lines between list items. Use vertical white space (16px - 24px) or alternating subtle background shifts (`surface` to `surface-container-low`).
*   **Medical Content Cards:** Use `xl` (1.5rem) rounding for outer containers and `md` (0.75rem) for nested elements like internal charts or patient photos.

### Contextual HMS Components
*   **Patient Timeline:** An asymmetrical vertical track using `secondary` (Teal) dots. Overlapping cards should use "Glassmorphism" to show depth.
*   **Dosage Badges:** High-contrast `tertiary` (#9E3D00) accents for urgent medications, using a "Soft Glow" (8% opacity shadow) to draw the doctor's eye without flashing.

---

## 6. Do's and Don'ts

### Do
*   **Do** use asymmetrical layouts on public/welcome pages to feel bespoke.
*   **Do** prioritize "Breathing Room." If a dashboard feels crowded, increase the `surface` padding rather than adding a divider.
*   **Do** use `backdrop-blur` on sidebars to keep the user grounded in their previous context.

### Don't
*   **Don't** use pure black (#000000). Use `on-surface` (#191C1D) for high-end legibility.
*   **Don't** use 1px borders to separate content. Let the color blocks do the work.
*   **Don't** use standard "Drop Shadows." Only use "Ambient Glows" with large blur and low opacity.
*   **Don't** use sharp corners. Everything in a hospital should feel safe and approachable—round every corner to at least 8px (`DEFAULT`).