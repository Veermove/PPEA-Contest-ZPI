import { Col, Row } from "react-bootstrap";
import { IconType } from "react-icons";
import Tile from "./tile";

interface TileProps {
  text: string;
  Icon: IconType;
  onClick: () => void;
}

function DoubleTile({ firstTileProps, secondTileProps }: { firstTileProps: TileProps, secondTileProps: TileProps }) {
  return (
    <Row className="my-2 p-3">
      <Col xs={12} lg={6} className="btn btn-light btn-gray my-sm-2 my-lg-0" onClick={firstTileProps.onClick}>
        <Tile {...firstTileProps} />
      </Col>
      <Col xs={12} lg={6} className="btn btn-light btn-gray my-sm-2 my-lg-0" onClick={secondTileProps.onClick}>
        <Tile {...secondTileProps} />
      </Col>
    </Row>
  )
}

export default DoubleTile;
