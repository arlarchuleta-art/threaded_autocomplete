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

## Timeline
- Codebase finalized: 07/15/2026
- Testing and GitHub repository creation: 07/16/2026

*All code manually reviewed and documented with internal citations.*
