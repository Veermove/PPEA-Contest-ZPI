import { Col, Row } from "react-bootstrap";
import { IconType } from "react-icons";

function Tile({ text, Icon }: { text: string, Icon: IconType }) {
    return (
        <Row className="justify-content-around">
            <Col xs={4}>
                <div className="d-flex align-items-center h-100">
                    <h3 className="text-purple text-center">{text}</h3>
                </div>
            </Col>
            <Col xs={2}>
                <Icon className="text-purple" size={72} />
            </Col>
        </Row>
    )
}

export default Tile
