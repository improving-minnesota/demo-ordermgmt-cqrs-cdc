import Image from "next/image";
import { motion } from "framer-motion";
import Spinner from "../ui/Spinner";

interface PaymentScreenProps {
  onNext: () => void;
  isLoading: boolean;
}

const PaymentScreen = ({ onNext, isLoading }: PaymentScreenProps) => (
  <motion.div
    className="card"
    initial={{ opacity: 0, x: 300 }}
    animate={{ opacity: 1, x: 0 }}
    exit={{ opacity: 0, x: -300 }}
    transition={{ duration: 0.5 }}
  >
    <h1 className="card-title">Penny on Us!!</h1>
    <div className="card-image-container">
      <Image
        src="/imgs/penny.png"
        alt="Penny"
        width={250}
        height={250}
        className="card-image"
      />
    </div>
    <button
      onClick={onNext}
      className="card-button"
      disabled={isLoading}
    >
      {isLoading ? (
        <>
          <Spinner /> <span className="ml-2">Processing...</span>
        </>
      ) : (
        "Complete WISH"
      )}
    </button>
  </motion.div>
);

export default PaymentScreen;
