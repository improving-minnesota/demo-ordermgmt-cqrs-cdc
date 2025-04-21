import Image from "next/image";
import { motion } from "framer-motion";
import Spinner from "../ui/Spinner";

interface LocationScreenProps {
  onNext: () => void;
  isLoading: boolean;
  userName: string;
  setUserName: (name: string) => void;
}

const LocationScreen = ({ onNext, isLoading, userName, setUserName }: LocationScreenProps) => (
  <motion.div
    className="card"
    initial={{ opacity: 0, x: 300 }}
    animate={{ opacity: 1, x: 0 }}
    exit={{ opacity: 0, x: -300 }}
    transition={{ duration: 0.5 }}
  >
    <h1 className="card-title">Where are You?</h1>
    <div className="card-image-container">
      <Image
        src="/imgs/location.png"
        alt="Location"
        width={250}
        height={250}
        className="card-image"
      />
    </div>
    <div className="mt-4 mb-4 w-full">
      <input
        type="text"
        value={userName}
        onChange={(e) => setUserName(e.target.value)}
        placeholder="Your Name (required)"
        className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
    </div>
    <button
      onClick={onNext}
      className="card-button"
      disabled={isLoading || !userName.trim()}
    >
      {isLoading ? (
        <>
          <Spinner /> <span className="ml-2">Processing...</span>
        </>
      ) : (
        "HERE I am"
      )}
    </button>
  </motion.div>
);

export default LocationScreen;
