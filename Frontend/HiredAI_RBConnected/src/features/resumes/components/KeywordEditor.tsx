import imgImage29 from "../assets/ed3a390e622d86ef712709ecc96909a91e5a9560.png";
import FormSidebar from "./FormSidebar";
import { useRef } from "react";
import { track } from "../../../utils/analytics";
import svgPaths from "../imports/svg-qru9qbdxdq";

interface KeywordEditorProps {
  selectedRole: string;
  selectedKeywords: string[];
  onSaveChanges?: () => void;
  onBack?: () => void;
}

/**
 * Renders the keyword editor page for reviewing a resume preview and selected keywords.
 *
 * @param selectedRole - The role shown in the page subtitle.
 * @param selectedKeywords - The keywords displayed in the selected keywords section.
 * @param onSaveChanges - Called after the save action is completed.
 * @param onBack - Called when a back action is triggered.
 * @returns The keyword editor UI.
 */
export function KeywordEditor({ selectedRole, selectedKeywords, onSaveChanges, onBack }: KeywordEditorProps) {
  const startTimeRef = useRef(Date.now());

  const handleSaveChanges = () => {
    const elapsed = Math.round((Date.now() - startTimeRef.current) / 1000);
    try {
      track("resume_builder_step_completed", {
        step: "keyword_editor",
        time_spent_seconds: elapsed,
        template_chosen: null,
      });
    } catch (e) {
      console.warn("Analytics error for keyword_editor step completed:", e);
    }
    onSaveChanges?.();
  };

  return (
    <div className="min-h-screen bg-white px-4 sm:px-6 lg:px-10 py-6 lg:py-12">
      <div className="max-w-[1728px] mx-auto">
        {/* Header */}
        <div className="flex items-center gap-4 mb-8 lg:mb-12">
          <button 
            onClick={onBack}
            className="flex items-center justify-center w-12 h-12 rounded-lg border border-neutral-800 bg-white shadow-[0_4px_12px_rgba(0,0,0,0.05)] hover:bg-gray-50 transition-colors"
            aria-label="Go back"
          >
            <svg className="w-10 h-10" fill="none" viewBox="0 0 40 40">
              <path 
                d="M25 30 L15 20 L25 10" 
                stroke="black" 
                strokeLinecap="round" 
                strokeLinejoin="round" 
                strokeWidth="3" 
              />
            </svg>
          </button>
          <div>
            <h1 className="font-['Poppins:Bold',sans-serif] text-[#262626] text-2xl sm:text-3xl lg:text-4xl">
              Add keywords to resume
            </h1>
            <p className="font-['Poppins:Medium',sans-serif] text-[rgba(65,65,65,0.7)] text-sm sm:text-base mt-1">
              {selectedRole}
            </p>
          </div>
        </div>
        
        {/* Main content area */}
        <div className="flex flex-col lg:flex-row gap-6 lg:gap-[71px] mb-6">
          {/* Left side - Form replaces previous profile/sidebar content */}
          <FormSidebar />
          
          {/* Right side - Resume preview */}
          <div className="w-full lg:w-[641px] bg-white rounded-lg overflow-hidden shadow-[0px_4px_20px_rgba(0,0,0,0.1)]">
            <img 
              alt="Resume preview with added keywords" 
              className="w-full h-auto object-cover" 
              src={imgImage29} 
            />
          </div>
        </div>
        
        {/* Selected keywords info */}
        {selectedKeywords.length > 0 && (
          <div className="mb-6">
            <p className="font-['Poppins:Medium',sans-serif] text-[#262626] text-base sm:text-lg mb-3">
              Adding keywords: {selectedKeywords.join(", ")}
            </p>
          </div>
        )}
        
        {/* Action buttons */}
        <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-4 mt-8">
          {/* Back button */}
          <button 
            onClick={onBack}
            className="min-w-[159px] rounded-lg border border-neutral-800 bg-white px-6 py-2.5 flex items-center justify-center gap-[13px] shadow-[0_4px_12px_rgba(0,0,0,0.05)] hover:bg-gray-50 transition-colors"
          >
            <svg className="w-[22px] h-[22px]" fill="none" viewBox="0 0 22 22">
              <path 
                d={svgPaths.p155100}
                stroke="black" 
                strokeLinecap="round" 
                strokeLinejoin="round" 
                strokeWidth="2" 
              />
            </svg>
            <span className="font-['Poppins:Medium',sans-serif] text-[20px] text-black leading-normal">
              Back
            </span>
          </button>
          
          {/* Save Changes button */}
          <button 
            onClick={handleSaveChanges}
            className="bg-black text-white rounded-lg px-6 py-2.5 flex items-center justify-center gap-2 hover:bg-gray-800 transition-colors min-w-[220px]"
          >
            <span className="font-['Poppins:Medium',sans-serif] text-[20px] leading-normal">
              Save Changes
            </span>
            <div className="rotate-180 w-[12.833px] h-[11px]">
              <svg className="block w-full h-full" fill="none" viewBox="0 0 14.8333 13">
                <path 
                  d={svgPaths.p13c60ae0}
                  stroke="white" 
                  strokeLinecap="round" 
                  strokeLinejoin="round" 
                  strokeWidth="2" 
                />
              </svg>
            </div>
          </button>
        </div>
      </div>
    </div>
  );
}
