import Image from "next/image";
import { motion } from "framer-motion";

const WishSentScreen = () => (
  <motion.div
    className="card"
    initial={{ opacity: 0, x: 300 }}
    animate={{ opacity: 1, x: 0 }}
    exit={{ opacity: 0, x: -300 }}
    transition={{ duration: 0.5 }}
  >
    <h1 className="card-title">Wish Sent...</h1>
    <div className="card-image-container">
      <Image
        src="/imgs/wish.png"
        alt="Wish"
        width={250}
        height={250}
        className="card-image"
      />
    </div>
    <p className="card-text">Good Luck!!</p>
  </motion.div>
);

export default WishSentScreen;
