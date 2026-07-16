# Threaded Autocomplete Lab

## Assignment Overview
Custom implementation of a threaded autocomplete engine for classic literature.

## Modification Log
- **Stop on Exclamation:** Implemented regex-based stop condition in `getTextToPeriod`.
- **Quit to Exit:** Added `System.exit(0)` trigger to stop the GUI when user types "quit".
- **Break Between Words:** Modified `wrappedToFit` to scan backwards for whitespace, preventing word-splitting.
- **Multiword Match:** Updated `getOptions` to dynamically match entire user input strings.
- **Three Button Output:** Added JButtons to export novel snippets to file.

## Project Status: Development Complete
- Initial implementation of threaded autocomplete engine.
- Completed core functionality:
    - Multi-threading for background novel parsing.
    - 'Stop on Exclamation' & 'Quit to Exit' features.
    - 'Break Between Words' and 'Multiword Match' search enhancements.
    - 'Three Button Output' export capability.
      
## Test 1 
- **Status:** Functional baseline established.
- **Verification:** Multi-threading and autocomplete triggers verified via Test 1 screenshot.
- **Usability Issue:** Individual "Save" buttons are correctly appending to `MashupStory-1st test.txt`, but this lacks granular control for the user.
- **Resolution Plan:** Refactor export logic to support individual logs for each novel, with a separate button reserved for the full mashup export.

## Test 2 Findings
- **Status:** 4-Button layout and routing functional.
- **Verification:** Verified individual files generate successfully, and the Mashup button combines all three correctly.
- **Usability Issue:** Individual log files do not state the novel name *inside* the text document itself, which could lead to confusion if text is copied out of the file.
- **Resolution Plan:** Update `saveIndividual` method to print the novel name inside the file before the prompt and snippet.

## Test 3 Findings
- **Status:** Export logic refined.
- **Verification:** Verified that each `_Log.txt` file now explicitly states the "Novel: [Name]" at the start of the snippet.
- **New Issue Identified:** The GUI currently uses `setLayout(null)`, which prevents the window from resizing when corners are dragged.
- **Resolution Plan:** Refactor GUI to use a responsive Layout Manager (BoxLayout) to allow for window resizing.

## Test 4 & 5 Findings (UI Polish)
- **Status:** Layout refined and stabilized.
- **Verification:** GUI now correctly resizes dynamically while retaining the original aesthetic.
- **Modifications:** Implemented `BoxLayout` with `EmptyBorder` and `LineBorder` to restore visual depth.
- **Verification:** Individual logs successfully inject novel identification markers.
-   
## Timeline
- Codebase finalized: 07/15/2026
- GitHub repository creation: 07/16/2026-9:30AM
- Testing Started: 07/16/2026-9:40AM
- Testing Findings: 07/16/2026 9:45AM
- SuggestionGUI updated as well tested: 07/16/2026-10:41AM
- Novel Verification: 07/16/2026-4:11PM
- UI Polish: 07/16/2025-6:30PM
  
*All code manually reviewed and documented with internal citations.*
